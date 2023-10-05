package com.jyujyu.dayonetest.model;

public class StudentScoreTestDataBuilder {
	public static StudentScore.StudentScoreBuilder passed() {
		return StudentScore.builder()
			.studentName("defaultName")
			.exam("defaultExam")
			.korScore(70)
			.englishScore(65)
			.mathScore(80);
	}

	public static StudentScore.StudentScoreBuilder filled() {
		return StudentScore.builder()
			.studentName("defaultName")
			.exam("defaultExam")
			.korScore(60)
			.englishScore(55)
			.mathScore(30);
	}
}
