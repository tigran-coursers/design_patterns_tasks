package com.example.di;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * TESTS FOR THE STRUCTURE OF THE CODE (ArchUnit).
 *
 * An ordinary test looks at what the code DOES. An ArchUnit test looks at who USES whom:
 * it reads the compiled classes and checks the rules of the design.
 * If a rule is broken, the message names the class and the line.
 *
 * PROVIDED CODE - do not change.
 */
@AnalyzeClasses(packages = "com.example.di", importOptions = ImportOption.DoNotIncludeTests.class)
class DependencyRulesTest {

    @ArchTest
    static final ArchRule level1_theServiceDoesNotKnowAnyImplementation =
            noClasses().that().haveFullyQualifiedName("com.example.di.level1.TaskService")
                    .should().dependOnClassesThat().resideInAPackage("..impl..")
                    .because("it must be given its helpers, not create them (Task 1)");

    @ArchTest
    static final ArchRule level4_onlyTheCompositionRootKnowsTheImplementations =
            noClasses().that().resideInAPackage("com.example.di.level4..")
                    .and().resideOutsideOfPackage("..wiring..")
                    .should().dependOnClassesThat().resideInAPackage("..impl..")
                    .because("only the composition root (package wiring) may say 'new' for an implementation (Task 4)");

    @ArchTest
    static final ArchRule level5_nobodyUsesTheServiceLocator =
            noClasses().that().resideInAPackage("..level5..")
                    .and().doNotHaveSimpleName("ServiceLocator")
                    .and().doNotHaveSimpleName("BadReportService")
                    .should().dependOnClassesThat().haveSimpleName("ServiceLocator")
                    .because("a Service Locator hides the dependencies. Use constructor injection (Task 5)");

    @ArchTest
    static final ArchRule theDomainDoesNotKnowTheImplementations =
            noClasses().that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat().resideInAPackage("..impl..")
                    .because("the interfaces must not know the classes that implement them");
}
