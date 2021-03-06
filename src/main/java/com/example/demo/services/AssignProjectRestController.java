/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.services;

import com.example.demo.Employee.Employee;
import com.example.demo.Employee.EmployeeRepository;
import com.example.demo.Log.Log;
import com.example.demo.Log.LogRepository;
import com.example.demo.project.Project;
import com.example.demo.project.ProjectRepository;
import java.util.Date;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 *
 * @author gabriel
 */
@RestController
@RequestMapping(path = "/assign/{employeeId}", produces = APPLICATION_JSON_VALUE)
public class AssignProjectRestController {

    private EmployeeRepository employeeRepository;
    private ProjectRepository projectRepository;
    private LogRepository logRepository;

    public AssignProjectRestController(EmployeeRepository employeeRepository, ProjectRepository projectRepository, LogRepository logRepository) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.logRepository = logRepository;
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> assign(@PathVariable Long employeeId, @RequestBody AssignDTO input) {
        logRepository.save(new Log(""
                + "Assigned project: " + input.getProjId()
                + " to employee:" + employeeId, new Date()));
        Employee employee = employeeRepository.findOne(
                employeeId);
        Project project = projectRepository.findOne(input.getProjId());

        if (employee == null || project == null) {
            return ResponseEntity.notFound().build();
        }
        if (employee.getProjects().size() < 2) {
            employee.addProject(project);
            return ResponseEntity.ok(this.employeeRepository.save(
                    employee));
        } else {
            return ResponseEntity.badRequest().build();
        }

    }
}
