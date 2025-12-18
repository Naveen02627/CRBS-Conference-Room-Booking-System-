package Techno.Carts.CRBS.Controller;

import Techno.Carts.CRBS.Entity.Department;
import Techno.Carts.CRBS.Services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/add")
    public ResponseEntity<Department> addNewDepartment(@RequestBody Department department){
        return departmentService.addNewDepartment(department);
    }

    @PostMapping("/update")
    public ResponseEntity<Department> updateDepartment(@RequestBody Department department){
        return departmentService.updateDepartment(department);
    }

    @GetMapping("/allDepartment")
    public ResponseEntity<List<Department>> getAllDepartment(){
        return departmentService.allDepartment();
    }

}