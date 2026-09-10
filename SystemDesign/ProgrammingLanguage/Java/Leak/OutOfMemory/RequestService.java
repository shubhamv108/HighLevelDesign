package SystemDesign.ProgrammingLanguage.Java.Leak.OutOfMemory;

import java.util.ArrayList;
import java.util.List;

public class RequestService {

    private static List<String> requests = new ArrayList<>();

    public void process(String request) {
        requests.add(request);  // never removed
    }
}