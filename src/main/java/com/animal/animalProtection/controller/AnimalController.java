package com.animal.animalProtection.controller;


import com.animal.animalProtection.model.Animal;
import com.animal.animalProtection.model.SendEmail;
import com.animal.animalProtection.model.User;
import com.animal.animalProtection.model.Volunteer;
import com.animal.animalProtection.repository.AnimalRepository;
import com.animal.animalProtection.repository.VolunteerRepository;
import com.animal.animalProtection.services.AnimalPDFService;
import com.animal.animalProtection.services.AnimalService;
import com.animal.animalProtection.services.DatabasePDFService;
import com.animal.animalProtection.services.VolunteerService;
import com.animal.animalProtection.services.impl.EmailService;
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
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class AnimalController {
    @Autowired
    private AnimalService animalService;

    @GetMapping("/animal")
    public String home(Model model) {
        return findPaginated(1, "name", "asc", model);
    }


    @GetMapping("/showAnimal")
    public String showAnimal(Model model) {
        Animal animal = new Animal();
        model.addAttribute("animal", animal);
        return "saveAnimal";
    }

    @PostMapping("/saveAnimal")
    public String addAnimal(@ModelAttribute("animal") Animal animal) {
        //Save animal to the database
        animalService.saveAnimal(animal);
        return "redirect:/animal";
    }

    @GetMapping("/showUpdateAnimal/{id}")
    public String showUpdateAnimal(@PathVariable(value = "id") long id,
                                   Model model) {
        Animal animal = animalService.getAnimalById(id);
        model.addAttribute("animal", animal);
        return "updateAnimal";
    }

    @GetMapping("/deleteAnimal/{id}")
    public String deleteAnimal(@PathVariable(value = "id") long id) {
        this.animalService.deleteAnimalById(id);
        return "redirect:/animal";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 3;

        Page<Animal> page = animalService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Animal> listAnimals = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("listAnimals", listAnimals);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");


        return "index";
    }


    @Autowired
    AnimalRepository animalRepository;

    @GetMapping(value = "/exportAnimalPdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> animalReport() throws IOException {
        List<Animal> animals = (List<Animal>) animalRepository.findAll();

        ByteArrayInputStream bis = AnimalPDFService.animalPDFReport(animals);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=AnimalReport.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/exportAnimalCsv")
    public void exportAnimalCSV(HttpServletResponse response)
            throws Exception {

        //set file name and content type
        String filename = "Animal-data.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");
        //create a csv writer
        StatefulBeanToCsv<Animal> writer = new StatefulBeanToCsvBuilder<Animal>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR).withOrderedResults(false)
                .build();
        //write all employees data to csv file
        writer.write(animalService.getAllAnimals());

    }

    @GetMapping("/searchAnimal")
    public String searchMethod(Model model) {
        model.addAttribute("search", new Animal());
        return "searchAnimal";
    }

    @PostMapping("/searchAnimal")
    public String getEmployee(@ModelAttribute("search") Animal animal, Model model) {
        Animal animal1 = animalService.getAnimalById(animal.getId());
        if (animal1 != null) {
            model.addAttribute("animal1", animal1);
            return "searchAnimal";
        } else {
            model.addAttribute("error", "He/She is not found");
            return "searchAnimal";
        }
    }

    @Autowired
    private EmailService emailService;
    @Autowired
    private VolunteerService volunteerService;

    @GetMapping("/sendEmail")
    public String sendEmail(Model model) {
        model.addAttribute("volunteer", volunteerService.getAllVolunteers());

        return "sendEmail";
    }

    @PostMapping("/postEmail")
    public String postEmail(@ModelAttribute SendEmail sendEmail, HttpSession session) {
        emailService.sendEmail(sendEmail);
        session.setAttribute("msg", "Email Sent Successfully");
        return "redirect:/sendEmail";

    }

}
