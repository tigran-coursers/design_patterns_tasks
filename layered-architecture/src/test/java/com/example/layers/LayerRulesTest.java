package com.example.layers;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.library.Architectures;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * TESTS FOR THE STRUCTURE (ArchUnit). They do not run your code: they read the compiled classes and check
 * WHO USES WHOM. The picture of the layers:
 *
 *     Presentation  ──►  Service  ──►  Repository
 *          │                │               │
 *          └────────────────┴───────────────┴──────►  Model
 *
 * An arrow is allowed. An arrow that goes UP or JUMPS OVER a layer is a violation.
 * When a rule fails, the message names the class and the line.
 *
 * PROVIDED CODE - do not change.
 */
@AnalyzeClasses(packages = "com.example.layers", importOptions = ImportOption.DoNotIncludeTests.class)
class LayerRulesTest {

    /** The same four layers, for a level like "com.example.layers.level2". */
    static Architectures.LayeredArchitecture layers(String level) {
        return Architectures.layeredArchitecture().consideringOnlyDependenciesInLayers()
                .layer("Presentation").definedBy(level + ".presentation..")
                .layer("Service").definedBy(level + ".service..")
                .layer("Repository").definedBy(level + ".repository..")
                .layer("Model").definedBy(level + ".model..")
                .whereLayer("Presentation").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Presentation")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service");
    }

    // ------------------------------------------------------------------ level 1: every class in its layer

    @ArchTest
    static final ArchRule level1_controllersAreInThePresentationLayer =
            classes().that().resideInAPackage("..level1..").and().haveSimpleNameEndingWith("Controller")
                    .should().resideInAPackage("com.example.layers.level1.presentation..")
                    .because("controllers talk to the user: the presentation layer (Task 1)");

    @ArchTest
    static final ArchRule level1_theConsoleAppIsOutsideTheLayers =
            classes().that().resideInAPackage("..level1..").and().haveSimpleName("ConsoleApp")
                    .should().resideInAPackage("com.example.layers.level1")
                    .because("the starting point connects ALL the layers, so it belongs to none of them. "
                            + "It lives in the package of the level itself (Task 1)");

    @ArchTest
    static final ArchRule level1_servicesAreInTheServiceLayer =
            classes().that().resideInAPackage("..level1..").and().haveSimpleNameEndingWith("Service")
                    .should().resideInAPackage("com.example.layers.level1.service..")
                    .because("the rules of the application: the service layer (Task 1)");

    @ArchTest
    static final ArchRule level1_repositoriesAreInTheRepositoryLayer =
            classes().that().resideInAPackage("..level1..").and().haveSimpleNameEndingWith("Repository")
                    .should().resideInAPackage("com.example.layers.level1.repository..")
                    .because("the storage: the repository (data) layer (Task 1)");

    @ArchTest
    static final ArchRule level1_theTaskIsInTheModel =
            classes().that().resideInAPackage("..level1..").and().haveSimpleName("Task")
                    .should().resideInAPackage("com.example.layers.level1.model..")
                    .because("the data that everybody shares: the model (Task 1)");

    @ArchTest
    static final ArchRule level1_theLayersObeyTheDependencyRule =
            layers("com.example.layers.level1").as("level 1: the layers obey the dependency rule");

    // ------------------------------------------------------------------ level 2: fix the two broken rules

    @ArchTest
    static final ArchRule level2_thePresentationLayerDoesNotTouchTheRepository =
            noClasses().that().resideInAPackage("..level2.presentation..")
                    .should().dependOnClassesThat().resideInAPackage("..level2.repository..")
                    .because("the presentation layer talks to the SERVICE, never to the data layer (Task 2, TODO 2 and 3)");

    @ArchTest
    static final ArchRule level2_theRepositoryDoesNotKnowThePresentationLayer =
            noClasses().that().resideInAPackage("..level2.repository..")
                    .should().dependOnClassesThat().resideInAPackage("..level2.presentation..")
                    .because("an arrow must never point UP: the data layer knows nothing about the screen (Task 2, TODO 1)");

    @ArchTest
    static final ArchRule level2_allTheLayerRules = layers("com.example.layers.level2").as("level 2: all the layer rules");

    // ------------------------------------------------------------------ level 3: do not leak the model

    private static final ArchCondition<JavaMethod> notShowTheModel = new ArchCondition<>("not show a model class in its signature") {
        @Override
        public void check(JavaMethod method, ConditionEvents events) {
            boolean shows = false;
            for (JavaClass type : method.getReturnType().getAllInvolvedRawTypes()) {
                shows |= type.getPackageName().endsWith(".model");
            }
            for (var parameter : method.getParameterTypes()) {
                for (JavaClass type : parameter.getAllInvolvedRawTypes()) {
                    shows |= type.getPackageName().endsWith(".model");
                }
            }
            events.add(new SimpleConditionEvent(method, !shows,
                    method.getFullName() + (shows ? " shows a model class (use a DTO)" : " is fine")));
        }
    };

    @ArchTest
    static final ArchRule level3_thePublicMethodsOfThePresentationLayerDoNotShowTheModel =
            methods().that().arePublic().and().areDeclaredInClassesThat().resideInAPackage("..level3.presentation..")
                    .and().areDeclaredInClassesThat().haveSimpleName("TaskController")
                    .should(notShowTheModel)
                    .because("the model is internal. The outside world gets DTOs (Task 3, TODO 1 and 2)");

    @ArchTest
    static final ArchRule level3_allTheLayerRules = layers("com.example.layers.level3").as("level 3: all the layer rules");

    // ------------------------------------------------------------------ levels 4, 5, 6: keep the rules while the code grows

    @ArchTest
    static final ArchRule level4_allTheLayerRules = layers("com.example.layers.level4").as("level 4: all the layer rules");

    @ArchTest
    static final ArchRule level5_allTheLayerRules = layers("com.example.layers.level5").as("level 5: all the layer rules");

    @ArchTest
    static final ArchRule level6_allTheLayerRules = layers("com.example.layers.level6").as("level 6: all the layer rules");

    @ArchTest
    static final ArchRule level6_theServiceDoesNotKnowTheAuditingRepository =
            noClasses().that().resideInAPackage("..level6.service..")
                    .should().dependOnClassesThat().haveSimpleName("AuditingTaskRepository")
                    .because("the service only knows the TaskRepository INTERFACE: it must not care that a decorator wraps it (Task 6)");
}
