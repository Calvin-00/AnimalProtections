package com.animal.animalProtection.controller;

import com.animal.animalProtection.model.User;
import com.animal.animalProtection.model.Volunteer;
import com.animal.animalProtection.repository.PeopleRepo;
import com.animal.animalProtection.repository.VolunteerRepository;
import com.animal.animalProtection.services.DatabasePDFService;
import com.animal.animalProtection.services.PeoplePDFService;
import com.animal.animalProtection.services.PeopleService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class PeopleController {
    @Autowired
    private PeopleService peopleService;

    @GetMapping("/viewPeople")
    public String viewPeoplePage(Model model){
        model.addAttribute("listPeople", peopleService.getAllPeople());
        return "people";
    }

    @GetMapping("/showNewPeopleForm")
    public String showNewPeopleForm(Model model) {
        // create model attribute to bind form data
        User people = new User();
        model.addAttribute("people", people);
        return "new_employee";
    }
    @PostMapping("/savePeople")
    public String saveEmployee(@ModelAttribute("people") User user) {
        // save employee to database
        peopleService.savePeople(user);
        return "redirect:/viewPeople";
    }

    @GetMapping("/showPeopleForUpdate/{id}")
    public String showFormForUpdate(@PathVariable( value = "id") long id, Model model) {

        // get employee from the service
        User user = peopleService.getPeopleById(id);

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("user", user);
        return "updatePeople";
    }

    @GetMapping("/deletePeople/{id}")
    public String deletePeople(@PathVariable (value = "id") long id) {

        // call delete employee method
        this.peopleService.deletePeopleById(id);
        return "redirect:/viewPeople";
    }

    @Autowired
    PeopleRepo peopleRepo;
    @GetMapping(value = "/exportPeoplePdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> peopleReport()  throws IOException {
        List<User> users = (List<User>) peopleRepo.findAll();

        ByteArrayInputStream bis = PeoplePDFService.peoplePDFReport(users);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=UsersReport.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/exportPeopleCsv")
    public void exportPeopleCSV(HttpServletResponse response)
            throws Exception {

        //set file name and content type
        String filename = "Users-data.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");
        //create a csv writer
        StatefulBeanToCsv<User> writer = new StatefulBeanToCsvBuilder<User>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR).withOrderedResults(false)
                .build();
        //write all employees data to csv file
        writer.write(peopleService.getAllPeople());

    }

    @GetMapping ("/searchPeople")
    public String searchMethod(Model model){
        model.addAttribute("search",new User());
        return "searchPeople";
    }

    @PostMapping("/searchPeople")
    public String getEmployee(@ModelAttribute("search") User user, Model model){
        User user1=peopleService.getPeopleById(user.getId());
        if (user1!=null) {
            model.addAttribute("user1",user1);
            return "searchPeople";
        }else {
            model.addAttribute("error","He/She is not found");
            return "searchPeople";
        }
    }
}
