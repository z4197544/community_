//package life.zxw.community.community.advice;
//
//import life.zxw.community.community.exception.CustomizeException;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.ModelAndView;
//
//@ControllerAdvice
//public class CustomizeExceptionHandler {
//    @ExceptionHandler(Exception.class)
//    ModelAndView handleControllerException(Throwable ex, Model model) {
//        if (ex instanceof CustomizeException) {
//            model.addAttribute("message", ex.getMessage());
//        } else {
//            model.addAttribute("message", "冒烟了");
//        }
//        return new ModelAndView("error");
//    }
//
//
//}
