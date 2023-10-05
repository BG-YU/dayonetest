package com.jyujyu.dayonetest.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.jyujyu.dayonetest.controller.response.ExamFailStudentResponse;
import com.jyujyu.dayonetest.controller.response.ExamPassStudentResponse;
import com.jyujyu.dayonetest.model.StudentFail;
import com.jyujyu.dayonetest.model.StudentFailFixture;
import com.jyujyu.dayonetest.model.StudentPass;
import com.jyujyu.dayonetest.model.StudentPassFixture;
import com.jyujyu.dayonetest.model.StudentScore;
import com.jyujyu.dayonetest.model.StudentScoreFixture;
import com.jyujyu.dayonetest.model.StudentScoreTestDataBuilder;
import com.jyujyu.dayonetest.repository.StudentFailRepository;
import com.jyujyu.dayonetest.repository.StudentPassRepository;
import com.jyujyu.dayonetest.repository.StudentScoreRepository;

class StudentScoreServiceTest {
	private StudentScoreService studentScoreService;
	private StudentScoreRepository studentScoreRepository;
	private StudentPassRepository studentPassRepository;
	private StudentFailRepository studentFailRepository;

	@BeforeEach
	public void beforeEach() {
		studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
		studentPassRepository = Mockito.mock(StudentPassRepository.class);
		studentFailRepository = Mockito.mock(StudentFailRepository.class);

		studentScoreService = new StudentScoreService(
			studentScoreRepository,
			studentPassRepository,
			studentFailRepository
		);
	}

	@Test
	@DisplayName("첫번째 Mock 테스트")
	public void firstSaveScoreMockTest() {
		// given
		String givenStudentName = "jyujyu";
		String givenExam = "testexam";
		Integer givenKorScore = 80;
		Integer givenEnglishScore = 100;
		Integer givenMathScore = 60;

		// when
		studentScoreService.saveScore(
			givenStudentName,
			givenExam,
			givenKorScore,
			givenEnglishScore,
			givenMathScore
		);
	}

	@Test
	@DisplayName("성적 저장 검증 / 60점 이상")
	public void saveScoreMockTest() {
		// given : 평균 점수가 60점 이상 저장 검증
		StudentScore expectStudentScore = StudentScoreTestDataBuilder.passed().build();
		StudentPass expectStudentPass = StudentPassFixture.create(expectStudentScore);

		ArgumentCaptor<StudentScore> studentScoreArgumentCaptor = ArgumentCaptor.forClass(StudentScore.class);
		ArgumentCaptor<StudentPass> studentPassArgumentCaptor = ArgumentCaptor.forClass(StudentPass.class);

		// when
		studentScoreService.saveScore(
			expectStudentScore.getStudentName(),
			expectStudentScore.getExam(),
			expectStudentScore.getKorScore(),
			expectStudentScore.getEnglishScore(),
			expectStudentScore.getMathScore()
		);

		// then
		Mockito.verify(studentScoreRepository, Mockito.times(1)).save(studentScoreArgumentCaptor.capture());
		StudentScore capturedStudentScore = studentScoreArgumentCaptor.getValue();
		Assertions.assertEquals(expectStudentScore.getStudentName(), capturedStudentScore.getStudentName());
		Assertions.assertEquals(expectStudentScore.getExam(), capturedStudentScore.getExam());
		Assertions.assertEquals(expectStudentScore.getKorScore(), capturedStudentScore.getKorScore());
		Assertions.assertEquals(expectStudentScore.getEnglishScore(), capturedStudentScore.getEnglishScore());
		Assertions.assertEquals(expectStudentScore.getMathScore(), capturedStudentScore.getMathScore());


		Mockito.verify(studentPassRepository, Mockito.times(1)).save(studentPassArgumentCaptor.capture());
		StudentPass capturedStudentPass = studentPassArgumentCaptor.getValue();
		Assertions.assertEquals(expectStudentPass.getStudentName(), capturedStudentPass.getStudentName());
		Assertions.assertEquals(expectStudentPass.getAvgScore(), capturedStudentPass.getAvgScore());
		Assertions.assertEquals(expectStudentPass.getExam(), capturedStudentPass.getExam());

		Mockito.verify(studentFailRepository, Mockito.times(0)).save(Mockito.any());
	}

	@Test
	@DisplayName("성적 저장 검증 / 60점 미만")
	public void saveScoreMockTest2() {
		// given : 평균 점수가 60점 미만 저장 검증
		StudentScore expectStudentScore = StudentScoreFixture.failed();
		StudentFail expectStudentPass = StudentFailFixture.create(expectStudentScore);

		ArgumentCaptor<StudentScore> studentScoreArgumentCaptor = ArgumentCaptor.forClass(StudentScore.class);
		ArgumentCaptor<StudentFail> studentFailArgumentCaptor = ArgumentCaptor.forClass(StudentFail.class);

		// when
		studentScoreService.saveScore(
			expectStudentScore.getStudentName(),
			expectStudentScore.getExam(),
			expectStudentScore.getKorScore(),
			expectStudentScore.getEnglishScore(),
			expectStudentScore.getMathScore()
		);

		// then
		Mockito.verify(studentScoreRepository, Mockito.times(1)).save(studentScoreArgumentCaptor.capture());
		StudentScore capturedStudentScore = studentScoreArgumentCaptor.getValue();
		Assertions.assertEquals(expectStudentScore.getStudentName(), capturedStudentScore.getStudentName());
		Assertions.assertEquals(expectStudentScore.getExam(), capturedStudentScore.getExam());
		Assertions.assertEquals(expectStudentScore.getKorScore(), capturedStudentScore.getKorScore());
		Assertions.assertEquals(expectStudentScore.getEnglishScore(), capturedStudentScore.getEnglishScore());
		Assertions.assertEquals(expectStudentScore.getMathScore(), capturedStudentScore.getMathScore());

		Mockito.verify(studentPassRepository, Mockito.times(0)).save(Mockito.any());

		Mockito.verify(studentFailRepository, Mockito.times(1)).save(studentFailArgumentCaptor.capture());
		StudentFail capturedStudentFail = studentFailArgumentCaptor.getValue();
		Assertions.assertEquals(expectStudentPass.getStudentName(), capturedStudentFail.getStudentName());
		Assertions.assertEquals(expectStudentPass.getAvgScore(), capturedStudentFail.getAvgScore());
		Assertions.assertEquals(expectStudentPass.getExam(), capturedStudentFail.getExam());
	}

	@Test
	@DisplayName("합격자 명단 가져오기")
	public void getPassStudentListTest() {
		// given
		String givenTestExam = "firstexam";
		StudentPass expectStudent1 = StudentPassFixture.create(givenTestExam, "test1");
		StudentPass expectStudent2 = StudentPassFixture.create(givenTestExam, "test2");
		StudentPass notExpectStudent3 = StudentPassFixture.create("secondexam", "test3");

		Mockito.when(studentPassRepository.findAll()).thenReturn(List.of(
			expectStudent1,
			expectStudent2,
			notExpectStudent3
		));

		// when
		List<ExamPassStudentResponse> expectPassList = List.of(expectStudent1, expectStudent2).stream()
			.map((pass) -> new ExamPassStudentResponse(pass.getStudentName(), pass.getAvgScore()))
			.toList();

		List<ExamPassStudentResponse> responses = studentScoreService.getPassStudentList(givenTestExam);

		// then
		Assertions.assertIterableEquals(expectPassList, responses);
	}

	@Test
	@DisplayName("불합격자 명단 가져오기")
	public void getFailStudentListTest() {
		// given
		String givenTestExam = "firstexam";
		StudentFail expectStudent1 = StudentFailFixture.create(givenTestExam, "test1");
		StudentFail expectStudent2 = StudentFailFixture.create(givenTestExam, "test2");
		StudentFail notExpectStudent3 = StudentFailFixture.create("secondexam", "test3");;
		Mockito.when(studentFailRepository.findAll()).thenReturn(List.of(
			expectStudent1,
			expectStudent2,
			notExpectStudent3
		));

		// when
		List<ExamFailStudentResponse> expectPassList = List.of(expectStudent1, expectStudent2)
			.stream()
			.map((fail) -> new ExamFailStudentResponse(fail.getStudentName(), fail.getAvgScore()))
			.toList();

		List<ExamFailStudentResponse> responses = studentScoreService.getFailStudentList(givenTestExam);

		// then
		Assertions.assertIterableEquals(expectPassList, responses);
	}
}
