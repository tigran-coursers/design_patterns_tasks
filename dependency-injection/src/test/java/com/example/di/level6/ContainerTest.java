package com.example.di.level6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Checks Task 6 (a tiny DI container). PROVIDED CODE - do not change. */
class ContainerTest {

    // ---- the classes that the container builds in the tests

    public interface Engine {
    }

    public static class PetrolEngine implements Engine {
        public PetrolEngine() {
        }
    }

    public static class Car {
        public final Engine engine;

        public Car(Engine engine) {
            this.engine = engine;
        }
    }

    public static class Garage {
        public final Car car;
        public final Engine engine;

        public Garage(Car car, Engine engine) {
            this.car = car;
            this.engine = engine;
        }
    }

    public static class A {
        public A(B b) {
        }
    }

    public static class B {
        public B(A a) {
        }
    }

    public static class X {
        public X(Y y) {
        }
    }

    public static class Y {
        public Y(Z z) {
        }
    }

    public static class Z {
        public Z(X x) {
        }
    }

    public static class TwoConstructors {
        public TwoConstructors() {
        }

        public TwoConstructors(Engine engine) {
        }
    }

    public static class NoPublicConstructor {
        private NoPublicConstructor() {
        }
    }

    public interface UnknownThing {
    }

    public static class NeedsUnknown {
        public NeedsUnknown(UnknownThing thing) {
        }
    }

    public static class FailsToStart {
        public FailsToStart() {
            throw new IllegalArgumentException("boom");
        }
    }

    // ---- the tests

    private Container container;

    @BeforeEach
    void setUp() {
        container = new Container();
    }

    @Test
    void get_buildsARegisteredConcreteClass() {
        container.register(PetrolEngine.class);

        assertNotNull(container.get(PetrolEngine.class));
    }

    @Test
    void get_buildsTheImplementationOfARegisteredAbstraction() {
        container.register(Engine.class, PetrolEngine.class);

        assertInstanceOf(PetrolEngine.class, container.get(Engine.class));
    }

    @Test
    void get_buildsTheDependenciesOfTheConstructor() {
        container.register(Engine.class, PetrolEngine.class);
        container.register(Car.class);

        Car car = container.get(Car.class);

        assertInstanceOf(PetrolEngine.class, car.engine);
    }

    @Test
    void get_goesDeep_dependenciesOfDependencies() {
        container.register(Engine.class, PetrolEngine.class);
        container.register(Car.class);
        container.register(Garage.class);

        Garage garage = container.get(Garage.class);

        assertNotNull(garage.car);
        assertInstanceOf(PetrolEngine.class, garage.car.engine);
    }

    @Test
    void get_isASingleton_theSameObjectEveryTime() {
        container.register(Engine.class, PetrolEngine.class);

        Engine first = container.get(Engine.class);

        assertNotNull(first);
        assertSame(first, container.get(Engine.class));
    }

    @Test
    void twoClassesThatNeedTheSameType_shareOneInstance() {
        container.register(Engine.class, PetrolEngine.class);
        container.register(Car.class);
        container.register(Garage.class);

        Garage garage = container.get(Garage.class);

        assertSame(garage.engine, garage.car.engine, "the Garage and its Car must use the SAME engine");
    }

    @Test
    void get_aTypeThatWasNeverRegistered_fails() {
        IllegalStateException error = assertThrows(IllegalStateException.class, () -> container.get(Engine.class));

        assertEquals("No binding for Engine", error.getMessage());
    }

    @Test
    void get_aMissingDependencyInTheMiddle_failsWithTheNameOfTheMissingType() {
        container.register(NeedsUnknown.class);

        IllegalStateException error = assertThrows(IllegalStateException.class, () -> container.get(NeedsUnknown.class));

        assertEquals("No binding for UnknownThing", error.getMessage());
    }

    @Test
    void get_aCircleOfTwo_isDetected() {
        container.register(A.class);
        container.register(B.class);

        IllegalStateException error = assertThrows(IllegalStateException.class, () -> container.get(A.class));

        assertEquals("Circular dependency: A -> B -> A", error.getMessage());
    }

    @Test
    void get_aCircleOfThree_isDetected() {
        container.register(X.class);
        container.register(Y.class);
        container.register(Z.class);

        IllegalStateException error = assertThrows(IllegalStateException.class, () -> container.get(X.class));

        assertEquals("Circular dependency: X -> Y -> Z -> X", error.getMessage());
    }

    @Test
    void afterAFailure_theContainerStillWorks() {
        container.register(A.class);
        container.register(B.class);
        container.register(Engine.class, PetrolEngine.class);
        assertThrows(IllegalStateException.class, () -> container.get(A.class));

        assertNotNull(container.get(Engine.class), "the failed get() must not leave a half-finished path behind");
        assertThrows(IllegalStateException.class, () -> container.get(A.class), "and the same error comes again");
    }

    @Test
    void get_aClassWithTwoPublicConstructors_fails() {
        container.register(TwoConstructors.class);

        IllegalStateException error = assertThrows(IllegalStateException.class, () -> container.get(TwoConstructors.class));

        assertEquals("TwoConstructors must have exactly one public constructor", error.getMessage());
    }

    @Test
    void get_aClassWithNoPublicConstructor_fails() {
        container.register(NoPublicConstructor.class);

        IllegalStateException error = assertThrows(IllegalStateException.class, () -> container.get(NoPublicConstructor.class));

        assertEquals("NoPublicConstructor must have exactly one public constructor", error.getMessage());
    }

    @Test
    void get_whenTheConstructorFails_theErrorSaysWhichClass() {
        container.register(FailsToStart.class);

        IllegalStateException error = assertThrows(IllegalStateException.class, () -> container.get(FailsToStart.class));

        assertEquals("Cannot create FailsToStart", error.getMessage());
    }
}
