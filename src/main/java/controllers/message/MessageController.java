
package controllers.message;

import controllers.AbstractController;
import domain.Actor;
import domain.Message;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.MessageService;

import javax.swing.*;
import java.util.Collection;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ActorService actorService;

    @ExceptionHandler(TypeMismatchException.class)
    public ModelAndView handleMismatchException(final TypeMismatchException oops) {
        JOptionPane.showMessageDialog(null, "Forbidden operation");
        return new ModelAndView("redirect:/");
    }

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(Collection.class, "recipients", new CustomCollectionEditor(Collection.class) {

            @Override
            protected Object convertElement(final Object element) {
                Integer id = null;

                if (element instanceof String && !((String) element).equals(""))
                    //From the JSP 'element' will be a String
                    try {
                        id = Integer.parseInt((String) element);
                    } catch (final NumberFormatException e) {
                        System.out.println("Element was " + ((String) element));
                        e.printStackTrace();
                    }
                else if (element instanceof Integer)
                    //From the database 'element' will be a Long
                    id = (Integer) element;

                return id != null ? MessageController.this.actorService.findOne(id) : null;
            }
        });
    }

    // List -------------------------------------------------------------
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        final ModelAndView result;
        Collection<Message> messages;

        try {
            final Actor principal = this.actorService.getActorLogged();
            messages = this.messageService.getMessagesByActor(principal.getId());
        } catch (final Exception e) {
            result = this.forbiddenOperation();
            return result;
        }

        result = new ModelAndView("message/list");
        result.addObject("messages", messages);
        result.addObject("requestURI", "message/list.do");

        return result;
    }

    // Create -----------------------------------------------------------
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView result;
        Message mesage;

        mesage = this.messageService.create();
        result = this.createModelAndView(mesage);

        return result;
    }

    // Create Broadcast ------------------------------------------------------------------------
    @RequestMapping(value = "/broadcast", method = RequestMethod.GET)
    public ModelAndView broadcast() {
        ModelAndView result;
        Message mesage;

        mesage = this.messageService.create();

        result = new ModelAndView("message/broadcast");
        result.addObject("mesage", mesage);

        return result;
    }

    // Send Broadcast  -------------------------------------------------------------
    @RequestMapping(value = "/broadcast", method = RequestMethod.POST, params = "send")
    public ModelAndView sendBroadcast(@ModelAttribute("mesage") final Message mesage, final BindingResult binding) {
        ModelAndView result;
        Message msg;

        try {
            mesage.setRecipients(this.actorService.findAll());
            msg = this.messageService.reconstruct(mesage, binding);
            if (binding.hasErrors())
                result = this.createModelAndView(mesage);
            else {
                this.messageService.save(msg);
                result = new ModelAndView("redirect:list.do");
            }
        } catch (final Throwable oops) {
            result = this.createModelAndView(mesage, "message.commit.error");
        }
        return result;
    }

    // Send -------------------------------------------------------------
    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView save(@ModelAttribute("mesage") final Message mesage, final BindingResult binding) {
        ModelAndView result;
        Message msg;

        try {
            msg = this.messageService.reconstruct(mesage, binding);
            if (binding.hasErrors())
                result = this.createModelAndView(mesage);
            else {
                this.messageService.save(msg);
                result = new ModelAndView("redirect:list.do");
            }
        } catch (final Throwable oops) {
            result = this.createModelAndView(mesage, "message.commit.error");
        }
        return result;
    }

    // Display ---------------------------------------
    @RequestMapping(value = "/display", method = RequestMethod.GET)
    public ModelAndView display(@RequestParam final int messageID) {
        ModelAndView result;
        Message message;

        try {
            final Actor principal = this.actorService.getActorLogged();
            message = this.messageService.findOne(messageID);
            Assert.isTrue(principal.getMessages().contains(message));
        } catch (final Exception e) {
            result = this.forbiddenOperation();
            return result;
        }

        result = new ModelAndView("message/display");
        result.addObject("mesage", message);
        result.addObject("mesageRecipients", message.getRecipients());

        return result;
    }

    // Delete ------------------------------------------------------
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam final int messageID) {
        ModelAndView result;
        Message message;

        try {
            try {
                final Actor principal = this.actorService.getActorLogged();
                message = this.messageService.findOne(messageID);
                Assert.isTrue(principal.getMessages().contains(message));
            } catch (final Exception e) {
                result = this.forbiddenOperation();
                return result;
            }
            this.messageService.delete(message);
            result = new ModelAndView("redirect:list.do");
        } catch (final Throwable oops) {
            message = this.messageService.findOne(messageID);
            result = this.createModelAndView(message, "messageBox.commit.error");
        }

        return result;
    }

    // Ancillary methods ------------------------------------------------------

    protected ModelAndView createModelAndView(final Message mesage) {
        ModelAndView result;

        result = this.createModelAndView(mesage, null);

        return result;
    }

    protected ModelAndView createModelAndView(final Message mesage, final String message) {
        ModelAndView result;

        final Collection<Actor> actorList = this.actorService.findAll();

        result = new ModelAndView("message/create");
        result.addObject("mesage", mesage);
        result.addObject("message", message);
        result.addObject("actorList", actorList);

        return result;
    }

    private ModelAndView forbiddenOperation() {
        return new ModelAndView("redirect:/message/list.do");
    }
}
