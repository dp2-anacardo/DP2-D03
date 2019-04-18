package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import domain.Company;
import domain.Hacker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Position;
import services.AdministratorService;
import services.PositionService;

@Controller
@RequestMapping("administrator")
public class DashboardAdministratorController extends AbstractController {

    @Autowired
    private AdministratorService	administratorService;
    @Autowired
    private PositionService			positionService;


    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard() {
        final ModelAndView result;

        /* Q1 */
        final Double avgNumberOfPositions = this.administratorService.getStatsPositionsPerCompany().get(0);
        final Double minNumberOfPositions = this.administratorService.getStatsPositionsPerCompany().get(1);
        final Double maxNumberOfPositions = this.administratorService.getStatsPositionsPerCompany().get(2);
        final Double stddevNumberOfPositions = this.administratorService.getStatsPositionsPerCompany().get(3);

        /* Q2 */
        final Double avgNumberOfApps = this.administratorService.getStatsApplicationsPerHacker().get(0);
        final Double minNumberOfApps = this.administratorService.getStatsApplicationsPerHacker().get(1);
        final Double maxNumberOfApps = this.administratorService.getStatsApplicationsPerHacker().get(2);
        final Double stddevNumberOfApps = this.administratorService.getStatsApplicationsPerHacker().get(3);

        /* Q3 */
        final Company companyWithMorePositionsOffered = this.administratorService.getCompanyWithMorePositionsOffered();

        /* Q4 */
        final Hacker hackerWithMoreApplications = this.administratorService.getHackerWithMoreApplications();

        /* Q5 */
        final Double avgSalaryOffered = this.administratorService.getStatsSalaryOffered().get(0);
        final Double minSalaryOffered = this.administratorService.getStatsSalaryOffered().get(1);
        final Double maxSalaryOffered = this.administratorService.getStatsSalaryOffered().get(2);
        final Double stddevSalaryOffered = this.administratorService.getStatsSalaryOffered().get(3);

        /* Q6 */
        final Collection<Object> bestPositionSalaryOffered = this.administratorService.getBestPositionSalaryOffered();

        /* Q7 */
        final Collection<Object> worstPositionSalaryOffered = this.administratorService.getWorstPositionSalaryOffered();

        /* Q8 */
        final Double avgNumOfCurricula = this.administratorService.getStatsOfCurricula().get(0);
        final Double minNumOfCurricula = this.administratorService.getStatsOfCurricula().get(1);
        final Double maxNumOfCurricula = this.administratorService.getStatsOfCurricula().get(2);
        final Double stddevNumOfCurricula = this.administratorService.getStatsOfCurricula().get(3);

        /* Q9 */
        final Double avgResultsOfFinder = this.administratorService.getStatsOfFinder().get(0);
        final Double minResultsOfFinder = this.administratorService.getStatsOfFinder().get(1);
        final Double maxResultsOfFinder = this.administratorService.getStatsOfFinder().get(2);
        final Double stddevResultsOfFinder = this.administratorService.getStatsOfFinder().get(3);

        /* Q12 */
        final Double RatioOfNotEmptyFinders = this.administratorService.getRatioOfNotEmptyFinders();
        final Double RatioOfEmptyFinders = this.administratorService.getRatioOfEmptyFinders();

        //HISTOGRAM
        final String language = LocaleContextHolder.getLocale().getLanguage();
        final List<String> positions2 = new ArrayList<>();
        final List<Position> positions = (List<Position>) this.positionService.findAll();
        String s = "";
        positions.remove(this.positionService.getDefaultPosition());
        final List<Integer> HistogramOfPositions = new ArrayList<>();
        for (final Position p : positions) {
            HistogramOfPositions.add(this.administratorService.getHistogramOfPositions(p.getRoleEn(), p.getRoleEs()));
            if (language.equals("es"))
                if (p != positions.get(positions.size() - 1))
                    s = s + '"' + p.getRoleEs() + '"' + ",";
                else
                    s = s + '"' + p.getRoleEs() + '"';

            else
                positions2.add(p.getRoleEn());
        }

        /* Q13 */
        final Double AvgRecordsPerHistory = this.administratorService.getAvgRecordsPerHistory();
        final Double MaxRecordsPerHistory = this.administratorService.getMaxRecordsPerHistory();
        final Double MinRecordsPerHistory = this.administratorService.getMinRecordsPerHistory();
        final Double StddevRecordsPerHistory = this.administratorService.getStddevRecordsPerHistory();

        /* Q14 */

        final String BrotherhoodWithLargestHistory = this.administratorService.getBrotherhoodWithLargestHistory();

        /* Q15 */
        final List<Brotherhood> BrotherhoodHistoryLargerThanAvg = this.administratorService.getBrotherhoodHistoryLargerThanAvg();

        /* Q16 */
        final Double RatioAreaNotCoordinatesByChapter = this.administratorService.getRatioAreaNotCoordinatesByChapter();

        /* Q17 */
        final Double AvgParadesCoordinatesByChapters = this.administratorService.getAvgParadesCoordinatesByChapters();
        final Double MinParadesCoordinatesByChapter = this.administratorService.getMinParadesCoordinatesByChapters();
        final Double MaxParadesCoordinatesByChapters = this.administratorService.getMaxParadesCoordinatesByChapters();
        final Double StddevParadesCoordinatesByChapters = this.administratorService.getStddevParadesCoordinatesByChapters();

        /* Q18 */

        final List<Chapter> ChaptersCoordinate10MoreParadesThanAvg = this.administratorService.getChaptersCoordinate10MoreParadesThanAvg();

        /* Q19 */
        final Double RatioParadeDraftVsFinal = this.administratorService.getRatioParadeDraftVsFinal();

        /* Q20 */
        final Double RatioParadeFinalModeAccepted = this.administratorService.getRatioParadeFinalModeAccepted();
        final Double RatioParadeFinalModeSubmitted = this.administratorService.getRatioParadeFinalModeSubmitted();
        final Double RatioParadeFinalModeRejected = this.administratorService.getRatioParadeFinalModeRejected();

        /* Q21 */
        final Double RatioActiveSponsorships = this.administratorService.getRatioActiveSponsorships();

        /* Q22 */

        final Double AvgSponsorshipsPerSponsor = this.administratorService.getAvgSponsorshipsPerSponsor();
        final Double MinSponsorshipsActivesPerSponsor = this.administratorService.getMinSponsorshipsActivesPerSponsor();
        final Double MaxSponsorshipsActivesPerSponsor = this.administratorService.getMaxSponsorshipsActivesPerSponsor();
        final Double StddevSponsorshipsActivesPerSponsor = this.administratorService.getStddevSponsorshipsActivesPerSponsor();

        /* Q23 */
        final List<String> Top5SponsorsInTermsOfSponsorshipsActives = this.administratorService.getTop5SponsorsInTermsOfSponsorshipsActives();

        result = new ModelAndView("administrator/dashboard");

        result.addObject("AvgOfMembersPerBrotherhood", AvgOfMembersPerBrotherhood);
        result.addObject("MinOfMembersPerBrotherhood", MinOfMembersPerBrotherhood);
        result.addObject("MaxOfMembersPerBrotherhood", MaxOfMembersPerBrotherhood);
        result.addObject("SteddevOfMembersPerBrotherhood", SteddevOfMembersPerBrotherhood);

        result.addObject("LargestBrotherhood", LargestBrotherhood);

        result.addObject("SmallestBrotherhoood", SmallestBrotherhoood);

        result.addObject("ParadeIn30Days", ParadeIn30Days);

        result.addObject("RatioOfRequestsApproveds", RatioOfRequestsApproveds);
        result.addObject("RatioOfRequestsPendings", RatioOfRequestsPendings);
        result.addObject("RatioOfRequestsRejecteds", RatioOfRequestsRejecteds);

        result.addObject("RatioOfRequestToParadePerAPPROVED", RatioOfRequestToParadePerAPPROVED);
        result.addObject("RatioOfRequestToParadePerREJECTED", RatioOfRequestToParadePerREJECTED);
        result.addObject("RatioOfRequestToParadePerPENDING", RatioOfRequestToParadePerPENDING);

        result.addObject("MembersAtLeast10PercentOfNumberOfRequestAccepted", MembersAtLeast10PercentOfNumberOfRequestAccepted);

        result.addObject("CountOfBrotherhoodPerArea", CountOfBrotherhoodPerArea);
        result.addObject("MinBrotherhoodPerArea", MinBrotherhoodPerArea);
        result.addObject("MaxBrotherhoodPerArea", MaxBrotherhoodPerArea);
        result.addObject("AvgBrotherhoodPerArea", AvgBrotherhoodPerArea);
        result.addObject("StddevBrotherhoodPerArea", StddevBrotherhoodPerArea);

        result.addObject("MinResultFinder", MinResultFinder);
        result.addObject("MaxResultFinder", MaxResultFinder);
        result.addObject("AvgResultFinder", AvgResultFinder);
        result.addObject("StddevResultFinder", StddevResultFinder);

        result.addObject("procesiones", procesiones);
        result.addObject("areas", areas);

        result.addObject("RatioOfNotEmptyFinders", RatioOfNotEmptyFinders);
        result.addObject("RatioOfEmptyFinders", RatioOfEmptyFinders);
        result.addObject("HistogramOfPositions", HistogramOfPositions);
        result.addObject("positions2", positions2);

        result.addObject("AvgRecordsPerHistory", AvgRecordsPerHistory);
        result.addObject("MaxRecordsPerHistory", MaxRecordsPerHistory);
        result.addObject("MinRecordsPerHistory", MinRecordsPerHistory);
        result.addObject("StddevRecordsPerHistory", StddevRecordsPerHistory);

        result.addObject("BrotherhoodHistoryLargerThanAvg", BrotherhoodHistoryLargerThanAvg);

        result.addObject("RatioAreaNotCoordinatesByChapter", RatioAreaNotCoordinatesByChapter);

        result.addObject("AvgParadesCoordinatesByChapters", AvgParadesCoordinatesByChapters);
        result.addObject("MinParadesCoordinatesByChapter", MinParadesCoordinatesByChapter);
        result.addObject("MaxParadesCoordinatesByChapters", MaxParadesCoordinatesByChapters);
        result.addObject("StddevParadesCoordinatesByChapters", StddevParadesCoordinatesByChapters);

        result.addObject("RatioParadeDraftVsFinal", RatioParadeDraftVsFinal);

        result.addObject("RatioParadeFinalModeAccepted", RatioParadeFinalModeAccepted);
        result.addObject("RatioParadeFinalModeSubmitted", RatioParadeFinalModeSubmitted);
        result.addObject("RatioParadeFinalModeRejected", RatioParadeFinalModeRejected);

        result.addObject("RatioActiveSponsorships", RatioActiveSponsorships);

        result.addObject("AvgSponsorshipsPerSponsor", AvgSponsorshipsPerSponsor);
        result.addObject("MinSponsorshipsActivesPerSponsor", MinSponsorshipsActivesPerSponsor);
        result.addObject("MaxSponsorshipsActivesPerSponsor", MaxSponsorshipsActivesPerSponsor);
        result.addObject("StddevSponsorshipsActivesPerSponsor", StddevSponsorshipsActivesPerSponsor);

        result.addObject("Top5SponsorsInTermsOfSponsorshipsActives", Top5SponsorsInTermsOfSponsorshipsActives);

        result.addObject("BrotherhoodWithLargestHistory", BrotherhoodWithLargestHistory);

        result.addObject("ChaptersCoordinate10MoreParadesThanAvg", ChaptersCoordinate10MoreParadesThanAvg);

        return result;
    }
}
