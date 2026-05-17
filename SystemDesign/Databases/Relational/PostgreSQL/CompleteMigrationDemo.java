package SystemDesign.Databases.Relational.PostgreSQL;


import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Complete working example demonstrating online database migration
 * This demo uses in-memory databases for easy testing
 */
public class CompleteMigrationDemo {
//    private static final // logger // logger = // loggerFactory.get// logger(CompleteMigrationDemo.class);

    public static void main(String[] args) {
//        // logger.info("========================================");
//        // logger.info("Database Migration - Complete Demo");
//        // logger.info("========================================\n");

        try {
            // Step 1: Setup
            DemoSetup setup = new DemoSetup();
            setup.createSourceDatabase();
            setup.createTargetDatabase();
            setup.insertSampleData(50000); // 50K rows

            // Step 2: Start simulating live traffic
            TrafficSimulator traffic = new TrafficSimulator(setup.getSourceConnection());
            traffic.start();

            // Step 3: Execute migration
            DemoMigration migration = new DemoMigration(
                    setup.getSourceConnection(),
                    setup.getTargetConnection()
            );
            migration.execute();

            // Step 4: Let CDC catch up
            // logger.info("\n=== Allowing CDC to catch up for 5 seconds ===");
            Thread.sleep(5000);

            // Step 5: Stop traffic and validate
            traffic.stop();
            migration.stopCDC();

            // Step 6: Validate
            // logger.info("\n=== Final Validation ===");
            long sourceCount = migration.getRowCount(setup.getSourceConnection());
            long targetCount = migration.getRowCount(setup.getTargetConnection());

            // logger.info("Source rows: {}", sourceCount);
            // logger.info("Target rows: {}", targetCount);
            // logger.info("Match: {}", sourceCount == targetCount ? "✓ YES" : "✗ NO");

            // logger.info("\n=== CDC Statistics ===");
            // logger.info("Events processed: {}", migration.getCdcEventsProcessed());
            // logger.info("Events skipped: {}", migration.getCdcEventsSkipped());

            // logger.info("\n========================================");
            // logger.info("Demo Complete!");
            // logger.info("========================================");

        } catch (Exception e) {
            // logger.error("Demo failed", e);
            System.exit(1);
        }
    }
}

/**
 * Sets up source and target databases with schema
 */
class DemoSetup {
    private static final // logger // logger = // loggerFactory.get// logger(DemoSetup.class);
    private Connection sourceConn;
    private Connection targetConn;

    public void createSourceDatabase() throws SQLException {
        // logger.info("Creating source database...");
        sourceConn = DriverManager.getConnection("jdbc:h2:mem:source;DB_CLOSE_DELAY=-1");

        Statement stmt = sourceConn.createStatement();
        stmt.execute("""
            CREATE TABLE users (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(100) NOT NULL,
                email VARCHAR(255) NOT NULL,
                age INT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """);

        // logger.info("✓ Source database created");
    }

    public void createTargetDatabase() throws SQLException {
        // logger.info("Creating target database...");
        targetConn = DriverManager.getConnection("jdbc:h2:mem:target;DB_CLOSE_DELAY=-1");

        Statement stmt = targetConn.createStatement();
        stmt.execute("""
            CREATE TABLE users (
                id BIGINT PRIMARY KEY,
                username VARCHAR(100) NOT NULL,
                email VARCHAR(255) NOT NULL,
                age INT,
                created_at TIMESTAMP,
                updated_at TIMESTAMP
            )
        """);

        // logger.info("✓ Target database created");
    }

    public void insertSampleData(int rows) throws SQLException {
        // logger.info("Inserting {} sample rows...", rows);

        PreparedStatement pstmt = sourceConn.prepareStatement(
                "INSERT INTO users (username, email, age, created_at, updated_at) VALUES (?, ?, ?, ?, ?)"
                                                             );

        for (int i = 1; i <= rows; i++) {
            pstmt.setString(1, "user_" + i);
            pstmt.setString(2, "user_" + i + "@example.com");
            pstmt.setInt(3, 20 + (i % 50));
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            pstmt.addBatch();

            if (i % 5000 == 0) {
                pstmt.executeBatch();
                // logger.info("  Inserted {} rows...", i);
            }
        }
        pstmt.executeBatch();
        sourceConn.commit();

        // logger.info("✓ Sample data inserted");
    }

    public Connection getSourceConnection() { return sourceConn; }
    public Connection getTargetConnection() { return targetConn; }
}

/**
 * Simulates live traffic by making random updates to source database
 */
class TrafficSimulator {
    private static final // logger // logger = // loggerFactory.get// logger(TrafficSimulator.class);
    private final Connection conn;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running = false;
    private final AtomicLong operationsCount = new AtomicLong(0);

    public TrafficSimulator(Connection conn) {
        this.conn = conn;
    }

    public void start() {
        // logger.info("Starting live traffic simulator...");
        running = true;

        executor.submit(() -> {
            try {
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE users SET age = ?, updated_at = ? WHERE id = ?"
                                                                    );

                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO users (username, email, age, created_at, updated_at) VALUES (?, ?, ?, ?, ?)"
                                                                    );

                PreparedStatement deleteStmt = conn.prepareStatement(
                        "DELETE FROM users WHERE id = ?"
                                                                    );

                while (running) {
                    int operation = (int) (Math.random() * 100);

                    if (operation < 70) {
                        // 70% updates
                        int id = 1 + (int) (Math.random() * 40000);
                        updateStmt.setInt(1, 20 + (int) (Math.random() * 50));
                        updateStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        updateStmt.setInt(3, id);
                        updateStmt.executeUpdate();

                    } else if (operation < 95) {
                        // 25% inserts
                        long count = operationsCount.incrementAndGet();
                        insertStmt.setString(1, "live_user_" + count);
                        insertStmt.setString(2, "live_user_" + count + "@example.com");
                        insertStmt.setInt(3, 25 + (int) (Math.random() * 40));
                        insertStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                        insertStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                        insertStmt.executeUpdate();

                    } else {
                        // 5% deletes
                        int id = 45000 + (int) (Math.random() * 5000);
                        deleteStmt.setInt(1, id);
                        deleteStmt.executeUpdate();
                    }

                    conn.commit();
                    Thread.sleep(10); // 100 ops/second

                    if (operationsCount.get() % 100 == 0) {
                        // logger.debug("Traffic: {} operations", operationsCount.get());
                    }
                }

            } catch (Exception e) {
                if (running) {
                    // logger.error("Traffic simulator error", e);
                }
            }
        });

        // logger.info("✓ Traffic simulator started");
    }

    public void stop() {
        // logger.info("Stopping traffic simulator...");
        running = false;
        executor.shutdown();
        // logger.info("✓ Traffic stopped. Total operations: {}", operationsCount.get());
    }
}

/**
 * Demonstrates the complete migration process
 */
class DemoMigration {
    private static final // logger // logger = // loggerFactory.get// logger(DemoMigration.class);

    private final Connection sourceConn;
    private final Connection targetConn;
    private final DemoSnapshotManager snapshotManager;
    private final DemoCDCConsumer cdcConsumer;

    public DemoMigration(Connection sourceConn, Connection targetConn) {
        this.sourceConn = sourceConn;
        this.targetConn = targetConn;
        this.snapshotManager = new DemoSnapshotManager(sourceConn);
        this.cdcConsumer = new DemoCDCConsumer(sourceConn, targetConn);
    }

    public void execute() throws Exception {
        // logger.info("\n=== STEP 1: Capture Snapshot Metadata ===");
        DemoSnapshotMetadata metadata = snapshotManager.captureSnapshot();
        // logger.info("Snapshot timestamp: {}", metadata.getTimestamp());
        // logger.info("Snapshot ID: {}", metadata.getSnapshotId());

        // logger.info("\n=== STEP 2: Start CDC Consumer ===");
        cdcConsumer.start(metadata);
        Thread.sleep(500); // Let CDC initialize

        // logger.info("\n=== STEP 3: Execute Parallel Snapshot ===");
        executeParallelSnapshot(metadata);

        // logger.info("\n=== STEP 4: Snapshot Complete ===");
        snapshotManager.releaseSnapshot();
    }

    private void executeParallelSnapshot(DemoSnapshotMetadata metadata) throws Exception {
        // Get table size
        Statement stmt = sourceConn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MIN(id), MAX(id) FROM users");
        rs.next();
        long minId = rs.getLong(1);
        long maxId = rs.getLong(2);

        // logger.info("Table range: {} to {}", minId, maxId);

        int chunkSize = 10000;
        int workers = 4;

        List<DemoSnapshotWorker> workerList = new ArrayList<>();
        int workerNum = 1;

        for (long startId = minId; startId <= maxId; startId += chunkSize) {
            long endId = Math.min(startId + chunkSize, maxId + 1);
            workerList.add(new DemoSnapshotWorker(
                    sourceConn, targetConn, metadata, startId, endId, workerNum++
            ));
        }

        // logger.info("Created {} chunks", workerList.size());

        ExecutorService executor = Executors.newFixedThreadPool(workers);
        List<Future<Integer>> futures = new ArrayList<>();

        for (DemoSnapshotWorker worker : workerList) {
            futures.add(executor.submit(worker));
        }

        int totalRows = 0;
        for (Future<Integer> future : futures) {
            totalRows += future.get();
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        // logger.info("✓ Snapshot complete: {} rows copied", totalRows);
    }

    public void stopCDC() throws Exception {
        cdcConsumer.stop();
    }

    public long getRowCount(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
        rs.next();
        return rs.getLong(1);
    }

    public long getCdcEventsProcessed() {
        return cdcConsumer.getEventsProcessed();
    }

    public long getCdcEventsSkipped() {
        return cdcConsumer.getEventsSkipped();
    }
}

/**
 * Snapshot metadata with timestamp-based filtering
 */
class DemoSnapshotMetadata {
    private final String snapshotId;
    private final Instant timestamp;

    public DemoSnapshotMetadata(String snapshotId, Instant timestamp) {
        this.snapshotId = snapshotId;
        this.timestamp = timestamp;
    }

    public String getSnapshotId() { return snapshotId; }
    public Instant getTimestamp() { return timestamp; }

    public boolean isAfterSnapshot(Timestamp eventTime) {
        return eventTime.toInstant().isAfter(timestamp);
    }
}

/**
 * Manages snapshot creation
 */
class DemoSnapshotManager {
    private static final // logger // logger = // loggerFactory.get// logger(DemoSnapshotManager.class);
    private final Connection conn;
    private Instant snapshotTime;

    public DemoSnapshotManager(Connection conn) {
        this.conn = conn;
    }

    public DemoSnapshotMetadata captureSnapshot() throws SQLException {
        // In a real PostgreSQL implementation, this would:
        // 1. Create replication slot
        // 2. Export snapshot
        // 3. Capture LSN

        // For demo: just capture current timestamp
        snapshotTime = Instant.now();
        String snapshotId = "demo-snapshot-" + System.currentTimeMillis();

        // logger.info("Snapshot captured at: {}", snapshotTime);

        return new DemoSnapshotMetadata(snapshotId, snapshotTime);
    }

    public void releaseSnapshot() {
        // logger.info("Snapshot released");
    }
}

/**
 * Worker that copies a chunk of data
 */
class DemoSnapshotWorker implements Callable<Integer> {
    private static final // logger // logger = // loggerFactory.get// logger(DemoSnapshotWorker.class);

    private final Connection sourceConn;
    private final Connection targetConn;
    private final DemoSnapshotMetadata metadata;
    private final long startId;
    private final long endId;
    private final int workerNum;

    public DemoSnapshotWorker(Connection sourceConn, Connection targetConn,
                              DemoSnapshotMetadata metadata, long startId, long endId, int workerNum) {
        this.sourceConn = sourceConn;
        this.targetConn = targetConn;
        this.metadata = metadata;
        this.startId = startId;
        this.endId = endId;
        this.workerNum = workerNum;
    }

    @Override
    public Integer call() throws Exception {
        // logger.info("[Worker {}] Processing chunk: {} - {}", workerNum, startId, endId);

        int rowsCopied = 0;

        // Read from source with snapshot isolation
        Statement stmt = sourceConn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT * FROM users WHERE id >= " + startId + " AND id < " + endId + " ORDER BY id"
                                        );

        // Prepare insert for target
        PreparedStatement insertStmt = targetConn.prepareStatement(
                """
                MERGE INTO users (id, username, email, age, created_at, updated_at)
                KEY(id) VALUES (?, ?, ?, ?, ?, ?)
                """
                                                                  );

        while (rs.next()) {
            insertStmt.setLong(1, rs.getLong("id"));
            insertStmt.setString(2, rs.getString("username"));
            insertStmt.setString(3, rs.getString("email"));
            insertStmt.setInt(4, rs.getInt("age"));
            insertStmt.setTimestamp(5, rs.getTimestamp("created_at"));
            insertStmt.setTimestamp(6, rs.getTimestamp("updated_at"));
            insertStmt.addBatch();
            rowsCopied++;

            if (rowsCopied % 1000 == 0) {
                insertStmt.executeBatch();
                targetConn.commit();
            }
        }

        insertStmt.executeBatch();
        targetConn.commit();

        // logger.info("[Worker {}] ✓ Completed: {} rows", workerNum, rowsCopied);
        return rowsCopied;
    }
}

/**
 * CDC consumer that captures and applies changes
 */
class DemoCDCConsumer {
    private static final // logger // logger = // loggerFactory.get// logger(DemoCDCConsumer.class);

    private final Connection sourceConn;
    private final Connection targetConn;
    private DemoSnapshotMetadata metadata;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running = false;
    private final AtomicLong eventsProcessed = new AtomicLong(0);
    private final AtomicLong eventsSkipped = new AtomicLong(0);

    // Track changes for demo (in real CDC, this comes from WAL/binlog)
    private final ConcurrentLinkedQueue<ChangeEvent> changeQueue = new ConcurrentLinkedQueue<>();

    public DemoCDCConsumer(Connection sourceConn, Connection targetConn) {
        this.sourceConn = sourceConn;
        this.targetConn = targetConn;
    }

    public void start(DemoSnapshotMetadata metadata) {
        this.metadata = metadata;
        running = true;

        // Start change capture (simulated)
        startChangCapture();

        // Start change application
        executor.submit(() -> {
            while (running) {
                try {
                    ChangeEvent event = changeQueue.poll();
                    if (event != null) {
                        processEvent(event);
                    } else {
                        Thread.sleep(10);
                    }
                } catch (Exception e) {
                    if (running) {
                        // logger.error("CDC consumer error", e);
                    }
                }
            }
        });

        // logger.info("✓ CDC consumer started");
    }

    private void startChangCapture() {
        // In a real implementation, this would:
        // - Connect to replication slot
        // - Read WAL/binlog changes
        // - Parse and enqueue change events

        // For demo: use trigger-like mechanism
        new Thread(() -> {
            try {
                Statement stmt = sourceConn.createStatement();

                // Poll for changes (simplified CDC simulation)
                while (running) {
                    // In real CDC, we'd read from replication slot
                    // Here we simulate by periodically checking updated_at
                    ResultSet rs = stmt.executeQuery(
                            "SELECT * FROM users WHERE updated_at > DATEADD('SECOND', -2, CURRENT_TIMESTAMP) LIMIT 100"
                                                    );

                    while (rs.next()) {
                        ChangeEvent event = new ChangeEvent(
                                "UPDATE",
                                rs.getTimestamp("updated_at"),
                                rs.getLong("id"),
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getInt("age"),
                                rs.getTimestamp("created_at"),
                                rs.getTimestamp("updated_at")
                        );
                        changeQueue.offer(event);
                    }

                    Thread.sleep(100);
                }
            } catch (Exception e) {
                if (running) {
                    // logger.error("Change capture error", e);
                }
            }
        }).start();
    }

    private void processEvent(ChangeEvent event) throws SQLException {
        // Filter: only apply events AFTER snapshot timestamp
        if (!metadata.isAfterSnapshot(event.eventTime)) {
            eventsSkipped.incrementAndGet();
            return;
        }

        // Apply change to target
        PreparedStatement pstmt = targetConn.prepareStatement(
                """
                MERGE INTO users (id, username, email, age, created_at, updated_at)
                KEY(id) VALUES (?, ?, ?, ?, ?, ?)
                """
                                                             );

        pstmt.setLong(1, event.id);
        pstmt.setString(2, event.username);
        pstmt.setString(3, event.email);
        pstmt.setInt(4, event.age);
        pstmt.setTimestamp(5, event.createdAt);
        pstmt.setTimestamp(6, event.updatedAt);
        pstmt.executeUpdate();
        targetConn.commit();

        long processed = eventsProcessed.incrementAndGet();
        if (processed % 100 == 0) {
            // logger.debug("CDC: Processed {} events, skipped {}", processed, eventsSkipped.get());
        }
    }

    public void stop() {
        // logger.info("Stopping CDC consumer...");
        running = false;
        executor.shutdown();
        // logger.info("✓ CDC stopped. Processed: {}, Skipped: {}",
                    eventsProcessed.get(), eventsSkipped.get());
    }

    public long getEventsProcessed() { return eventsProcessed.get(); }
    public long getEventsSkipped() { return eventsSkipped.get(); }
}

/**
 * Represents a change event from CDC
 */
class ChangeEvent {
    final String operation;
    final Timestamp eventTime;
    final long id;
    final String username;
    final String email;
    final int age;
    final Timestamp createdAt;
    final Timestamp updatedAt;

    public ChangeEvent(String operation, Timestamp eventTime, long id, String username,
                       String email, int age, Timestamp createdAt, Timestamp updatedAt) {
        this.operation = operation;
        this.eventTime = eventTime;
        this.id = id;
        this.username = username;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}