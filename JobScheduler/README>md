## Fn Req
1. create/schedule a job (immediate/ future/reoccurring)
2. Monitor the status of the job

### Out of scope
3. dependencies or DAGs

## Non Fn Req
1. availability > consistency (if not mission critical)
2. execute jobs within 2 secs of scheduled time
3. scalability
4. At-least-once execution

## Estimation
1. How many jobs per sec ? 10K jobs per sec

## Entities
1. Task {
    id
    params[] {}
}
2. Job {
    taskId
    userid
    schedule {
        type: DATE | CRON
        expression: '* * 10 * 5' | timestamp (min hour dat month dayofweek)
    }
}
3. Execution {
    time (Primary Key)
    executionTime (Sort ley)
    jobId
    userId
    status
    attempt
}
4. Account


## APIs
create Job
```
POST /jobs -> Job {
    taskId
    schedule
    params[] {  }
}
```

monitor jobs
```
GET /jobs?statu&start&end&queryTerm&page&size -> Jobs[]
```

## Data Flow
1. client -> Schedule -> ScheduleDB(job, Task, ) <- TaskExecutor





