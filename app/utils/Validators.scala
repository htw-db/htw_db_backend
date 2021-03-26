package utils

object Validators {
  def validateName(text: String): Boolean = {
    if (!text.forall(_.isLetterOrDigit)) {
      return false
    } else if (text.length < 4 ) {
      return false
    } else if (text.length > 20) {
      return false
    }
    text == text.toLowerCase()
  }
}
