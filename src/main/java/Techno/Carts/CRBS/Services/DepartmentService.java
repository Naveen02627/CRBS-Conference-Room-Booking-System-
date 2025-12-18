package Techno.Carts.CRBS.Services;

import Techno.Carts.CRBS.Entity.Department;

import Techno.Carts.CRBS.Repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    public ResponseEntity<Department> addNewDepartment(Department department) {
        Optional<Department> department1 = departmentRepository.findByDepartmentName(department.getDepartmentName());
        if (department1.isPresent()) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
        }
        Department saveDepartment = departmentRepository.save(department);

        return ResponseEntity.ok(saveDepartment);

    }

    public ResponseEntity<Department> updateDepartment(Department department) {

        Optional<Department> existingOpt = departmentRepository.findById(department.getId());

        if (existingOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        Department existingDepartment = existingOpt.get();


        existingDepartment.setDepartmentName(department.getDepartmentName());

        Department updatedDepartment = departmentRepository.save(existingDepartment);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedDepartment);
    }
    public ResponseEntity<List<Department>> allDepartment(){
        List<Department> deptList = departmentRepository.findAll();
        if(deptList.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return ResponseEntity.of(Optional.of(deptList));
    }


}
