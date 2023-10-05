package com.jyujyu.dayonetest.controller.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
public class ExamPassStudentResponse {
	private final String studentName;
	private final Double avgScore;
}
