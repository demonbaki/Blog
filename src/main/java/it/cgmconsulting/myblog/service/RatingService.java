package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Rating;
import it.cgmconsulting.myblog.repository.RatingRepository;
import org.springframework.stereotype.Service;


@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public void save(Rating r){
        ratingRepository.save(r);
    }
}
