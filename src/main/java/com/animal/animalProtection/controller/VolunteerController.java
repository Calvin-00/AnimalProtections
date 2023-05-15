package com.animal.animalProtection.controller;



import com.animal.animalProtection.model.Animal;
import com.animal.animalProtection.model.Volunteer;
import com.animal.animalProtection.repository.VolunteerRepository;
import com.animal.animalProtection.services.DatabasePDFService;
import com.animal.animalProtection.services.VolunteerService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class VolunteerController {
    @Autowired
    private VolunteerService volunteerService;

    @GetMapping("/volunteer")
    public String home(Model model){

        return findPaginatedVolunteer(1, "name", "asc", model);
    }

    @GetMapping("/showVolunteer")
    public String showVolunteer(Model model){
        Volunteer volunteer = new Volunteer();
        model.addAttribute("volunteer",volunteer);
        return "saveVolunteer";
    }

    @PostMapping("/saveVolunteer")
    public String addVolunteer(@ModelAttribute("volunteer") Volunteer volunteer){
        //Save animal to the database
        volunteerService.saveVolunteer(volunteer);
        return "redirect:/volunteer";
    }

    @GetMapping("/showUpdateVolunteer/{id}")
    public String showUpdateVolunteer(@PathVariable(value = "id") long id,
                                   Model model){
        Volunteer volunteer = volunteerService.getVolunteerById(id);
        model.addAttribute("volunteer",volunteer);
        return "updateVolunteer";
    }

    @GetMapping("/deleteVolunteer/{id}")
    public String deleteVolunteer(@PathVariable (value = "id") long id){
        this.volunteerService.deleteVolunteerById(id);
        return "redirect:/volunteer";
    }

    @GetMapping("/pageVolunteer/{pageNoVolunteer}")
    public String findPaginatedVolunteer(@PathVariable(value = "pageNoVolunteer") int pageNoVolunteer,
                                @RequestParam("sortFieldVolunteer") String sortFieldVolunteer,
                                @RequestParam("sortDirVolunteer") String sortDirVolunteer,
                                Model model) {
        int pageSizeVolunteer = 3;

        Page< Volunteer > page = volunteerService.findPaginated(pageNoVolunteer, pageSizeVolunteer, sortFieldVolunteer, sortDirVolunteer);
        List< Volunteer > listVolunteers = page.getContent();

        model.addAttribute("currentPageVolunteer", pageNoVolunteer);
        model.addAttribute("totalPagesVolunteer", page.getTotalPages());
        model.addAttribute("totalItemsVolunteer", page.getTotalElements());

        model.addAttribute("sortFieldVolunteer", sortFieldVolunteer);
        model.addAttribute("sortDirVolunteer", sortDirVolunteer);
        model.addAttribute("listVolunteers", listVolunteers);
        model.addAttribute("reverseSortDirVolunteer", sortDirVolunteer.equals("asc") ? "desc" : "asc");


        return "indexVolunteer";
    }

    @Autowired
    VolunteerRepository volunteerRepository;
    @GetMapping(value = "/exportPdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> volunteerReport()  throws IOException {
        List<Volunteer> volunteers = (List<Volunteer>) volunteerRepository.findAll();

        ByteArrayInputStream bis = DatabasePDFService.employeePDFReport(volunteers);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=VolunteerReport.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/exportCsv")
    public void exportCSV(HttpServletResponse response)
            throws Exception {

        //set file name and content type
        String filename = "Volunteer-data.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");
        //create a csv writer
        StatefulBeanToCsv<Volunteer> writer = new StatefulBeanToCsvBuilder<Volunteer>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR).withOrderedResults(false)
                .build();
        //write all employees data to csv file
        writer.write(volunteerService.getAllVolunteers());

    }

    @GetMapping ("/searchVolunteer")
    public String searchMethod(Model model){
        model.addAttribute("search",new Volunteer());
        return "searchVolunteer";
    }

    @PostMapping("/searchVolunteer")
    public String getEmployee(@ModelAttribute("search") Volunteer volunteer, Model model){
        Volunteer volunteer1=volunteerService.getVolunteerById(volunteer.getId());
        if (volunteer1!=null) {
            model.addAttribute("volunteer1",volunteer1);
            return "searchVolunteer";
        }else {
            model.addAttribute("error","He/She is not found");
            return "searchVolunteer";
        }
    }

}
