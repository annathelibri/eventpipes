package pw.aru.libs.eventpipes;

public interface Objects {
    Any any = new Any() {};
    A a = new A() {};
    AA aa = new AA();
    A1 a1 = new A1() {};
    AA1 aa1 = new AA1();
    B b = new B() {};
    BB bb = new BB();
    B1 b1 = new B1() {};
    BB1 bb1 = new BB1();

    interface Any {}

    interface A extends Any {}

    interface A1 extends A {}

    interface B extends Any {}

    interface B1 extends B {}

    class AA implements A {}

    class AA1 implements A1 {}

    class BB implements B {}

    class BB1 implements B1 {}
}
