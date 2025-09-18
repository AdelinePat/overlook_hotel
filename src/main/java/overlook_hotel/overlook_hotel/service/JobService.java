package overlook_hotel.overlook_hotel.service;

import org.springframework.stereotype.Service;
import overlook_hotel.overlook_hotel.model.entity.Job;
import overlook_hotel.overlook_hotel.repository.JobRepository;

import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job findJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    public List<Job> getFullJobList() {
        return jobRepository.findAll();
    }

}
