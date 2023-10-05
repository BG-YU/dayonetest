package com.jyujyu.dayonetest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jyujyu.dayonetest.MyCalculator;
import com.jyujyu.dayonetest.controller.response.ExamFailStudentResponse;
import com.jyujyu.dayonetest.controller.response.ExamPassStudentResponse;
import com.jyujyu.dayonetest.model.StudentFail;
import com.jyujyu.dayonetest.model.StudentPass;
import com.jyujyu.dayonetest.model.StudentScore;
import com.jyujyu.dayonetest.repository.StudentFailRepository;
import com.jyujyu.dayonetest.repository.StudentPassRepository;
import com.jyujyu.dayonetest.repository.StudentScoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentScoreService {
	private final StudentScoreRepository studentScoreRepository;
	private final StudentPassRepository studentPassRepository;
	private final StudentFailRepository studentFailRepository;

	public void saveScore(String studentName, String exam, Integer korScore, Integer englishScore, Integer mathScore) {
		StudentScore studentScore = StudentScore.builder()
			.studentName(studentName)
			.exam(exam)
			.korScore(korScore)
			.englishScore(englishScore)
			.mathScore(mathScore)
			.build();

		studentScoreRepository.save(studentScore);

		MyCalculator calculator = new MyCalculator(0.0);
		Double avgScore = calculator
			.add(korScore.doubleValue())
			.add(englishScore.doubleValue())
			.add(mathScore.doubleValue())
			.divide(3.0)
			.getResult();

		if (avgScore >= 60) {
			StudentPass studentPass = StudentPass.builder()
				.studentName(studentName)
				.exam(exam)
				.avgScore(avgScore)
				.build();
			studentPassRepository.save(studentPass);
		} else {
			StudentFail studentFail = StudentFail.builder()
				.studentName(studentName)
				.exam(exam)
				.avgScore(avgScore)
				.build();
			studentFailRepository.save(studentFail);
		}
	}

	public List<ExamPassStudentResponse> getPassStudentList(String exam) {
		List<StudentPass> studentPasses = studentPassRepository.findAll();

		return studentPasses.stream()
			.filter((pass) -> pass.getExam().equals(exam))
			.map((pass) -> new ExamPassStudentResponse(pass.getStudentName(), pass.getAvgScore()))
			.toList();
	}

	public List<ExamFailStudentResponse> getFailStudentList(String exam) {
		List<StudentFail> studentFails = studentFailRepository.findAll();

		return studentFails.stream()
			.filter((fail) -> fail.getExam().equals(exam))
			.map((fail) -> new ExamFailStudentResponse(fail.getStudentName(), fail.getAvgScore()))
			.toList();
	}
}
