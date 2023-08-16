package com.auction.flab.application.web.controller;

import com.auction.flab.application.service.ProjectApplicantService;
import com.auction.flab.application.vo.ProjectApplicantVo;
import com.auction.flab.application.web.dto.ProjectApplicantSearchResponseDto;
import com.auction.flab.application.web.dto.ProjectApplicantsSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProjectApplicantController {

    private final ProjectApplicantService projectApplicantService;

    @GetMapping("/projects/{id}/applicants")
    public ResponseEntity<List<ProjectApplicantsSearchResponseDto>> getProjectApplicants(@PathVariable Long id) {
        return new ResponseEntity<>(projectApplicantService.getProjectApplicants(id), HttpStatus.OK);
    }

    @GetMapping("/projects/{project-id}/applicants/{applicant-id}")
    public ResponseEntity<ProjectApplicantSearchResponseDto> getProjectApplicant(@PathVariable("project-id") Long projectId, @PathVariable("applicant-id") Long applicantId) {
        ProjectApplicantVo projectApplicantVo = ProjectApplicantVo.from(projectId, applicantId);
        return new ResponseEntity<>(projectApplicantService.getProjectApplicant(projectApplicantVo), HttpStatus.OK);
    }

}
