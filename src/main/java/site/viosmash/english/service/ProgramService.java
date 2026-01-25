package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.ProgramCreateRequest;
import site.viosmash.english.dto.request.ProgramUpdateRequest;
import site.viosmash.english.dto.response.ProgramResponse;
import site.viosmash.english.entity.Program;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.ProgramRepository;
import site.viosmash.english.util.Util;
import site.viosmash.english.util.enums.Status;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final Util util;
    private final ProgramRepository programRepository;

    public Program create(ProgramCreateRequest request) {
        
        Program program = new Program();
        program.setTitle(request.getTitle());
        program.setOrder(request.getOrder());
        program.setStatus(Status.ACTIVE.getValue());
        
        programRepository.save(program);
        return program;
    }

    public Program update(Integer id, ProgramUpdateRequest request) {

        Program program = programRepository.findById(id)
            .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Program not found with ID: " + id));
        
        if(request.getOrder() != null) program.setOrder(request.getOrder());

        if(request.getTitle() != null && !request.getTitle().isEmpty()) program.setTitle(request.getTitle());

        if(request.getStatus() != null) program.setStatus(request.getStatus());

        programRepository.save(program);
        return program;
    }

    public Page<ProgramResponse> getList(Pageable pageable, String keyword, Integer status) {

        if(keyword != null) keyword = "%" + keyword.toLowerCase() + "%";
        else keyword = "%%";

        return this.programRepository.findAllByKeyword(pageable, keyword, status);
    }
}