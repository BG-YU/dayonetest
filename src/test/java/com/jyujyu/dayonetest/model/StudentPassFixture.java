package com.jyujyu.dayonetest.model;

import com.jyujyu.dayonetest.MyCalculator;

public class StudentPassFixture {

	public static StudentPass create(StudentScore studentScore) {
		var calculator = new MyCalculator(0.0);
		return StudentPass.builder()
			.exam(studentScore.getExam())
			.studentName(studentScore.getStudentName())
			.avgScore(
				calculator
					.add(studentScore.getKorScore().doubleValue())
					.add(studentScore.getEnglishScore().doubleValue())
					.add(studentScore.getMathScore().doubleValue())
					.divide(3.0)
					.getResult()
			)
			.build();
	}

	public static StudentPass create(String exam, String studentName) {
		return StudentPass.builder()
			.exam(exam)
			.studentName(studentName)
			.avgScore(68.0)
			.build();
	}
}
