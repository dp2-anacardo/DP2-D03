
package controllers.problem;

import controllers.AbstractController;
import domain.Actor;
import domain.Company;
import domain.Problem;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.ProblemService;

import javax.swing.*;
import java.util.Collection;

@Controller
@RequestMapping("/problem")
public class ProblemController extends AbstractController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ActorService actorService;

    @ExceptionHandler(TypeMismatchException.class)
    public ModelAndView handleMismatchException(final TypeMismatchException oops) {
        JOptionPane.showMessageDialog(null, "Forbidden operation");
        return new ModelAndView("redirect:/");
    }

    // List -------------------------------------------------------------
    @RequestMapping(value = "company/list", method = RequestMethod.GET)
    public ModelAndView list() {
        final ModelAndView result;
        Collection<Problem> problems;

        try {
            final Actor principal = this.actorService.getActorLogged();
            Assert.isTrue(principal instanceof Company);
            problems = this.problemService.findAllByCompany(principal.getId());
        } catch (final Exception e) {
            result = this.forbiddenOperation();
            return result;
        }

        result = new ModelAndView("problem/company/list");
        result.addObject("problems", problems);
        result.addObject("requestURI", "problem/company/list.do");

        return result;
    }

    // Create -----------------------------------------------------------
    @RequestMapping(value = "company/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView result;
        Problem problem;

        problem = this.problemService.create();
        result = this.createModelAndView(problem);

        return result;
    }


    // Save -------------------------------------------------------------
    @RequestMapping(value = "company/create", method = RequestMethod.POST, params = "save")
    public ModelAndView save(@ModelAttribute("problem") final Problem problem, final BindingResult binding) {
        ModelAndView result;
        Problem prblm;

        try {
            prblm = this.problemService.reconstruct(problem, binding);
            if (binding.hasErrors()) {
                result = this.createModelAndView(problem);
                for (final ObjectError e : binding.getAllErrors())
                    if (e.getDefaultMessage().equals("URL incorrecta") || e.getDefaultMessage().equals("Invalid URL"))
                        result.addObject("attachmentError", e.getDefaultMessage());
            } else {
                this.problemService.save(prblm);
                result = new ModelAndView("redirect:/list.do");
            }
        } catch (final Throwable oops) {
            result = this.createModelAndView(problem, "problem.commit.error");
        }
        return result;
    }

    // Display ---------------------------------------
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public ModelAndView display(@RequestParam final int problemID) {
        ModelAndView result;
        Problem problem;

        try {
            final Actor principal = this.actorService.getActorLogged();
            problem = this.problemService.findOne(problemID);
        } catch (final Exception e) {
            result = this.forbiddenOperation();
            return result;
        }

        result = new ModelAndView("problem/show");
        result.addObject("problem", problem);

        return result;
    }

//    // Delete ------------------------------------------------------
//    @RequestMapping(value = "/delete", method = RequestMethod.GET)
//    public ModelAndView delete(@RequestParam final int messageID) {
//        ModelAndView result;
//        Message message;
//
//        try {
//            try {
//                final Actor principal = this.actorService.getActorLogged();
//                message = this.messageService.findOne(messageID);
//                Assert.isTrue(principal.getMessages().contains(message));
//            } catch (final Exception e) {
//                result = this.forbiddenOperation();
//                return result;
//            }
//            this.messageService.delete(message);
//            result = new ModelAndView("redirect:list.do");
//        } catch (final Throwable oops) {
//            message = this.messageService.findOne(messageID);
//            result = this.createModelAndView(message, "messageBox.commit.error");
//        }
//
//        return result;
//    }
//
    // Ancillary methods ------------------------------------------------------

    protected ModelAndView createModelAndView(final Problem problem) {
        ModelAndView result;

        result = this.createModelAndView(problem, null);

        return result;
    }

    protected ModelAndView createModelAndView(final Problem problem, final String message) {
        ModelAndView result;

        result = new ModelAndView("problem/company/create");
        result.addObject("problem", problem);
        result.addObject("message", message);

        return result;
    }

    private ModelAndView forbiddenOperation() {
        return new ModelAndView("redirect:/message/list.do");
    }
}
