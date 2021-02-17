package controllers

import dao.{bookingDAO, cinemaDAO, movieDAO}
import models.{Cinema, bookingForm}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.{Inject, Singleton}

@Singleton
class BookingController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport{

  def form() = Action.async{
    implicit request: Request[AnyContent] =>
      cinemaDAO.getAllCinemas.map{
        results => Ok(views.html.ticketBooking(bookingForm.form, results))
      }
  }

  def inputBooking() = Action { implicit request: Request[AnyContent] =>
    println(bookingForm.form.bindFromRequest().get)
    bookingForm.form.bindFromRequest().fold({ formWithErrors => BadRequest(views.html.ticketBooking(formWithErrors, Seq[Cinema]()))},
      { widget => bookingDAO.add(widget)
        Redirect(routes.PayPalController.index())
    })
  }

}