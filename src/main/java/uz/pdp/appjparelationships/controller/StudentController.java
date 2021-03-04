package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDTO;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    SubjectRepository subjectRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAll(pageable);
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentListForFaculty(@PathVariable Integer facultyId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
    }

    //4. GROUP OWNER
    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentListForGroup(@PathVariable Integer groupId,
                                                  @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroupId(groupId, pageable);
    }

    @GetMapping("/{studentid}")
    public Student getStudentById(@PathVariable Integer studentid){
        Optional<Student> byId = studentRepository.findById(studentid);
        return byId.orElseGet(Student::new);
    }

    @DeleteMapping("/{studentId}")
    public String deleteStudentById(@PathVariable Integer studentId){
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isPresent()) {
            studentRepository.delete(studentOptional.get());
            return "O'chirildi";
        }
        return "O'chirilmadi";
    }

    @PostMapping()
    public String addStudent(@RequestBody StudentDTO studentDTO){
        Optional<Group> byId = groupRepository.findById(studentDTO.getGroupid());
        if (byId.isPresent()) {
            Address address = new Address();
            address.setCity(studentDTO.getCity());
            address.setStreet(studentDTO.getStreet());
            address.setDistrict(studentDTO.getDistrict());
            addressRepository.save(address);
            Student student = new Student();
            student.setAddress(address);
            student.setGroup(byId.get());
            student.setFirstName(studentDTO.getFirstName());
            student.setLastName(studentDTO.getLastName());


            List<Subject> subjectList = subjectRepository.findAll();
            List<Subject> studentsubjectList = new ArrayList<>();
            for (Integer integer : studentDTO.getSubjectsId()) {
                for (Subject subject : subjectList) {
                    if (subject.getId()==integer){
                        studentsubjectList.add(subject);
                        break;
                    }
                }
            }
            student.setSubjects(studentsubjectList);
            studentRepository.save(student);
            return "Qo'shildi";
        }
        return "Qo'shilmadi";
    }

    @PutMapping("/{studentId}")
    public String editStudent(@RequestBody StudentDTO studentDTO, @PathVariable Integer studentId){
        Optional<Group> byId = groupRepository.findById(studentDTO.getGroupid());
        Optional<Student> studentbyId = studentRepository.findById(studentId);
        if (byId.isPresent() && studentbyId.isPresent()) {
            Address address = studentbyId.get().getAddress();
            address.setCity(studentDTO.getCity());
            address.setStreet(studentDTO.getStreet());
            address.setDistrict(studentDTO.getDistrict());
            addressRepository.save(address);
            Student student = studentbyId.get();
            student.setAddress(address);
            student.setGroup(byId.get());
            student.setFirstName(studentDTO.getFirstName());
            student.setLastName(studentDTO.getLastName());


            List<Subject> subjectList = subjectRepository.findAll();
            List<Subject> studentsubjectList = studentbyId.get().getSubjects();
            for (Integer integer : studentDTO.getSubjectsId()) {
                for (Subject subject : subjectList) {
                    if (subject.getId()==integer){
                        studentsubjectList.add(subject);
                        break;
                    }
                }
            }
            student.setSubjects(studentsubjectList);
            studentRepository.save(student);
            return "O'zgartirildi!";
        }
        return "O'zgartirilmadi!";
    }
}
