package com.example.b2bcrm.deal;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.example.b2bcrm.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedData implements CommandLineRunner {

    private final DealRepository dealRepository;
    private final UserService userService;

    public SeedData(DealRepository dealRepository, UserService userService) {
        this.dealRepository = dealRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        userService.ensureDefaultUsers();

        if (dealRepository.count() > 0) {
            return;
        }

        dealRepository.save(createDeal(
            "Atlas Components",
            "Mina Park",
            "J. Kim",
            DealStage.REGISTRATION,
            "85000",
            Priority.MEDIUM,
            LocalDate.of(2026, 7, 15),
            "Confirm target plants and register supplier profile.",
            "",
            ""
        ));
        dealRepository.save(createDeal(
            "Northstar Logistics",
            "Daniel Cho",
            "S. Lee",
            DealStage.GO_NO_GO,
            "180000",
            Priority.HIGH,
            LocalDate.of(2026, 8, 1),
            "Review margin model before go/no-go meeting.",
            "Busan Logistics Hub",
            "Warehouse automation parts"
        ));
        dealRepository.save(createDeal(
            "HelioGrid Energy",
            "Avery Shin",
            "M. Han",
            DealStage.ACCESS,
            "310000",
            Priority.HIGH,
            LocalDate.of(2026, 9, 22),
            "Schedule technical workshop with procurement lead.",
            "Seoul HQ",
            "Solar inverter materials"
        ));
        dealRepository.save(createDeal(
            "Blue Harbor Foods",
            "Grace Moon",
            "J. Kim",
            DealStage.AWARD,
            "124000",
            Priority.MEDIUM,
            LocalDate.of(2026, 6, 28),
            "Send final commercial terms and implementation date.",
            "Incheon Plant",
            "Packaging line components"
        ));
    }

    private Deal createDeal(
        String company,
        String contact,
        String owner,
        DealStage stage,
        String value,
        Priority priority,
        LocalDate closeDate,
        String nextAction,
        String opportunityLocation,
        String expectedItems
    ) {
        Deal deal = new Deal();
        deal.setCompany(company);
        deal.setContact(contact);
        deal.setOwner(owner);
        deal.setStage(stage);
        deal.setValue(new BigDecimal(value));
        deal.setProbability(stage.getDefaultProbability());
        deal.setPriority(priority);
        deal.setCloseDate(closeDate);
        deal.setNextAction(nextAction);
        deal.setOpportunityLocation(opportunityLocation);
        deal.setExpectedItems(expectedItems);
        return deal;
    }
}
