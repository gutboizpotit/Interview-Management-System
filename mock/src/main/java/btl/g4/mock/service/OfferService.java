package btl.g4.mock.service;

import btl.g4.mock.dto.OfferDto;
import btl.g4.mock.entity.Offer;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface OfferService {
    Offer getOfferById(int id);
    void saveOffer(Offer offer);
    void saveOffer(OfferDto offerDto) throws ParseException;
    void updateOffer(int id, OfferDto offerDto) throws ParseException;
    void cancelOffer(int id);
    Page<Offer> searchOffers(String keyword, String department, String status, Pageable pageable);
    void exportOffer(String dateFrom, String dateTo, HttpServletResponse response) throws ParseException;
    List<Offer> getOffersWaiting();
}
