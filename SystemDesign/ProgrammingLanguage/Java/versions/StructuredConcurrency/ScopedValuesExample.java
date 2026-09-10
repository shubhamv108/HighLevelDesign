package SystemDesign.ProgrammingLanguage.Java.versions.StructuredConcurrency;

//import jdk.incubator.concurrent.ScopedValue;
//
//public class ScopedValuesExample {
//    private static final ScopedValue<String> X = ScopedValue.newInstance();
//
//    public static void main(String[] args) {
//        foo();
//    }
//
//    static void foo() {
//        ScopedValue.runWhere(X, "foo", () -> bar());
//    }
//
//    static void bar() {
//        System.out.println("X in bar(): " + X.get());
//        ScopedValue.runWhere(X, "bar", () -> baz());
//        System.out.println("X in bar(): " + X.get());
//    }
//
//    static void baz() {
//        System.out.println("X in baz(): " + X.get());
//    }
//}
