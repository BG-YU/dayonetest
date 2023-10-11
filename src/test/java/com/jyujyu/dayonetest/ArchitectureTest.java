package com.jyujyu.dayonetest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;

public class ArchitectureTest {

	private JavaClasses javaClasses;

	@BeforeEach
	void beforeEach() {
		javaClasses = new ClassFileImporter()
			.withImportOption(new ImportOption.DoNotIncludeTests())
			.importPackages("com.jyujyu.dayonetest");
	}

	@Test
	@DisplayName("controller 패키지의 클래스는 Api로 끝이 나야 하고, @RestController 애너테이션이 반드시 필요하다.")
	void controllerNamingAndAnnotatedTest() {
		ArchRule rule = classes()
			.that().resideInAnyPackage("..controller")
			.should().haveSimpleNameEndingWith("Api")
			.andShould().beAnnotatedWith(RestController.class);

		rule.check(javaClasses);
	}

	@Test
	@DisplayName("service 패키지의 클래스는 Service로 끝이 나야 하고, @Service 애너테이션이 반드시 필요하다.")
	void serviceNamingAndAnnotatedTest() {
		ArchRule rule = classes()
			.that().resideInAnyPackage("..service")
			.should().haveSimpleNameEndingWith("Service")
			.andShould().beAnnotatedWith(Service.class);

		rule.check(javaClasses);
	}

	@Test
	@DisplayName("repository 패키지의 클래스는 Repository로 끝나야 하고, interface여야 한다.")
	void repositoryTest() {
		ArchRule rule = classes()
			.that().resideInAnyPackage("..repository..")
			.should().haveSimpleNameEndingWith("Repository")
			.andShould().beInterfaces();

		rule.check(javaClasses);
	}

	@Disabled("아직 학습한 내용을 코딩하지 않음")
	@Test
	@DisplayName("config 패키지의 클래스는 Config로 끝나야 하고, @Configuration 애너테이션이 반드시 필요하다.")
	void configTest() {
		ArchRule rule = classes()
			.that().resideInAnyPackage("..config..")
			.should().haveSimpleNameEndingWith("Config")
			.andShould().beAnnotatedWith(Configuration.class);

		rule.check(javaClasses);
	}

	@Test
	@DisplayName("Controller는 Service와 Request / Response만 사용할 수 있다.")
	void controllerDependencyTest() {
		ArchRule rule = classes()
			.that().resideInAnyPackage("..controller")
			.should().dependOnClassesThat()
			.resideInAnyPackage("..request..", "..response..", "..service..");
	}

	@Test
	@DisplayName("Controller는 다른 클래스에 의존되어서는 안된다.")
	void controllerDependencyTest2() {
		ArchRule rule = classes()
			.that().resideInAnyPackage("..controller")
			.should().onlyHaveDependentClassesThat().resideInAnyPackage("..controller");

		rule.check(javaClasses);
	}

	@Test
	@DisplayName("Controller는 model을 사용할 수 없다.")
	void controllerDependencyTest3() {
		ArchRule rule = noClasses()
			.that().resideInAnyPackage("..controller")
			.should().dependOnClassesThat().resideInAnyPackage("..model..");

		rule.check(javaClasses);
	}

	@Test
	@DisplayName("Service는 Controller를 의존하면 안된다.")
	void serviceDependencyTest() {
		ArchRule rule = noClasses()
			.that().resideInAnyPackage("..service..")
			.should().dependOnClassesThat().resideInAnyPackage("..controller");

		rule.check(javaClasses);
	}

	@Test
	@DisplayName("Model은 오직 Service와 Repository에 의해 의존된다.")
	void modelDependencyTest() {
		ArchRule rule = classes()
			.that().resideInAnyPackage("..model..")
			.should().onlyHaveDependentClassesThat().resideInAnyPackage("..service..", "..repository..", "..model..");

		rule.check(javaClasses);
	}

	@Test
	@DisplayName("Model은 아무것도 의존하지 않는다.")
	void modelDependencyTest2() {
		ArchRule rule = classes()
			.that().resideInAnyPackage("..model..")
			.should().onlyDependOnClassesThat()
			.resideInAnyPackage("..model..", "java..", "jakarta..");

		rule.check(javaClasses);
	}
}

