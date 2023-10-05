package com.jyujyu.dayonetest.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SaveExamRequest {
	private final String studentName;
	private final Integer korScore;
	private final Integer englishScore;
	private final Integer mathScore;
}
