package com.test.roy.rest.spring.boot;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {
	@Autowired
    private EmployeeDAO employeeDao;
	
	private static Long hitCount=0l;
	
	private static Logger logger=LogManager.getLogger(EmployeeController.class);
	@GetMapping(path="", produces = "application/json")
    public EmployeesList getEmployees()
    {
        return employeeDao.getAllEmployees();
    }
	
	@GetMapping(path="/{id}", produces = "application/json")
	public Employee getEmployee(@PathVariable String id) throws InterruptedException {
		logger.log(Level.INFO,"Request Received For ID: {}",new Object[] {id});
		EmployeesList list=employeeDao.getAllEmployees();
		Iterator<Employee> it=list.getEmployeeList().iterator();
		Employee emp=null;
		while(it.hasNext()) {
			emp=it.next();
			if(emp.getId().intValue()==Integer.parseInt(id)) {
				synchronized (hitCount) {
					hitCount=hitCount.longValue()+1;
					logger.log(Level.INFO,"Count: {}, Match Found for Id:{}",new Object[] {hitCount,id});
				}
				
				break;
			}
		}
		
		logger.log(Level.INFO,"Request Processed For ID: {}",new Object[] {id});
		return emp;
		//return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(emp);
	}
	
	@GetMapping(value = "/image")
	public @ResponseBody ResponseEntity<byte[]> getImage(HttpServletResponse response) throws IOException {
		
	    InputStream in = new BufferedInputStream(
	            new FileInputStream("C:\\Users\\eroyshr\\Documents\\MyWork\\Projects\\Zain-KWT\\Repo\\scx\\scx-gui\\src\\main\\gui\\src\\assets\\img\\6892406-aerial-view-hd.jpg"));
	    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
	    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(IOUtils.toByteArray(in));
	}
	@PostMapping(path= "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addEmployee(@RequestBody Employee employee,@PathVariable String id,@RequestParam(name="Name") String name )
    {
        //Integer id = employeeDao.getAllEmployees().getEmployeeList().size() + 1;
        employee.setId(Integer.parseInt(id));
         logger.info("Employee ID Received: "+employee.getId()+" In Path: "+id+", Name Param: "+name);
        employeeDao.addEmployee(employee);
         
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                    .path("/{id}")
                                    .buildAndExpand(employee.getId())
                                    .toUri();
         
        return ResponseEntity.created(location).build();
    }
}
