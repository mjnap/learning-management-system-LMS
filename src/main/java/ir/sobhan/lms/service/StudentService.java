package ir.sobhan.lms.service;

import ir.sobhan.lms.model.entity.CourseSectionRegistration;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    public Double average(List<CourseSectionRegistration> courseList){

        String avg = String.format("%.2f", courseList.stream()
                .map(CourseSectionRegistration::getScore)
                .reduce(Double::sum)
                .orElseGet(() -> Double.NaN) / courseList.size());

        if(avg.equals("NaN"))
            return 0D;

        return Double.valueOf(avg);
    }
}
