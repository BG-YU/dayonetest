package com.jyujyu.dayonetest.model;

public class StudentScoreFixture {
	public static StudentScore passed() {
		return StudentScore.builder()
			.studentName("defaultName")
			.exam("defaultExam")
			.korScore(70)
			.englishScore(65)
			.mathScore(80)
			.build();
	}

	public static StudentScore failed() {
		return StudentScore.builder()
			.studentName("defaultName")
			.exam("defaultExam")
			.korScore(60)
			.englishScore(55)
			.mathScore(30)
			.build();
	}
}
