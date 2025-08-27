package org.ums.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ums.model.Admission;
import org.ums.model.Student;
import org.ums.repo.AdmissionRepository;
import org.ums.repo.StudentRepository;

@Service
public class AdmissionService {
    private final StudentRepository students;
    private final AdmissionRepository admissions;
 //   private final EventPublisher events;
    private final ObjectMapper mapper = new ObjectMapper();

    public AdmissionService(StudentRepository students, AdmissionRepository admissions /*EventPublisher events*/) {
        this.students = students;
        this.admissions = admissions;
        //this.events = events;
    }

    @Transactional
    public Admission apply(String username, String program) {
        Student s = students.findByUserName(username).orElseThrow();
        Admission a = new Admission();
        a.setStudentId(s.getId());
        a.setProgram(program);
        a.setStatus("PENDING");
        admissions.save(a);

      //  publish("AdmissionApplied", s.getId().toString(), a);
        return a;
    }

//    private void publish(String type, String aggregateId, Object payload) {
//        try {
//            String json = mapper.writeValueAsString(payload);
//            events.publish(DomainEvent.of(type, aggregateId, json, null));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}