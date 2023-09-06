package com.auction.flab.application.web.controller;

import com.auction.flab.application.service.ProjectApplicantService;
import com.auction.flab.application.vo.ProjectApplicantAddVo;
import com.auction.flab.application.vo.ProjectApplicantSelVo;
import com.auction.flab.application.web.dto.ProjectApplicantRequestDto;
import com.auction.flab.application.web.dto.ProjectApplicantSearchResponseDto;
import com.auction.flab.application.web.dto.ProjectApplicantsSearchResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProjectApplicantController {

    private final ProjectApplicantService projectApplicantService;

    @GetMapping("/projects/{project-id}/applicants")
    public ResponseEntity<List<ProjectApplicantsSearchResponseDto>> getProjectApplicants(@PathVariable("project-id")  Long projectId) {
        return new ResponseEntity<>(projectApplicantService.getProjectApplicants(projectId), HttpStatus.OK);
    }

    @GetMapping("/projects/{project-id}/applicants/{applicant-id}")
    public ResponseEntity<ProjectApplicantSearchResponseDto> getProjectApplicant(@PathVariable("project-id") Long projectId, @PathVariable("applicant-id") Long applicantId) {
        ProjectApplicantSelVo projectApplicantSelVo = ProjectApplicantSelVo.from(projectId, applicantId);
        return new ResponseEntity<>(projectApplicantService.getProjectApplicant(projectApplicantSelVo), HttpStatus.OK);
    }

    @PostMapping("/projects/{project-id}/applicants")
    public ResponseEntity<Void> addProjectApplicant(@PathVariable("project-id") Long projectId, @Valid @RequestBody ProjectApplicantRequestDto projectApplicantRequestDto) {
        ProjectApplicantAddVo projectApplicantVo = ProjectApplicantAddVo.from(projectId, projectApplicantRequestDto);
        projectApplicantService.addProjectApplicant(projectApplicantVo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/projects/{project-id}/applicants/{applicant-id}")
    public ResponseEntity<Void> confirmProjectApplicant(@PathVariable("project-id") Long projectId, @PathVariable("applicant-id") Long applicantId) {
        projectApplicantService.confirmProjectApplicant(projectId, applicantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
