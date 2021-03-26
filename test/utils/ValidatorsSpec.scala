package utils

import org.scalatestplus.play.PlaySpec

class ValidatorsSpec extends PlaySpec {

  "ValidatorsSpec#validateName" should {

    "validate lowercase" in {
      Validators.validateName("abcde") mustBe true
      Validators.validateName("wxyz23") mustBe true
      Validators.validateName("abcxyz32") mustBe true
      Validators.validateName("1abcxyz32") mustBe true
      Validators.validateName("1234") mustBe true
      Validators.validateName("0000") mustBe true
      Validators.validateName("false") mustBe true
      Validators.validateName("true") mustBe true

      Validators.validateName("AAAA") mustBe false
      Validators.validateName("AaAa") mustBe false
      Validators.validateName("abcA") mustBe false
      Validators.validateName("abcA1") mustBe false
    }

    "validate invalid characters" in {
      Validators.validateName("abc!") mustBe false
      Validators.validateName("!!!!") mustBe false
      Validators.validateName("!abc") mustBe false
      Validators.validateName("123?") mustBe false
      Validators.validateName("--\n") mustBe false
      Validators.validateName("\n\n\n\n") mustBe false
      Validators.validateName("ab cd") mustBe false
    }

    "validate minimum and maximum length" in {
      Validators.validateName("abc1") mustBe true
      Validators.validateName("123xyz") mustBe true
      Validators.validateName("abcxyzasdasdsadsads") mustBe true

      Validators.validateName("aaa") mustBe false
      Validators.validateName("123") mustBe false
      Validators.validateName("000") mustBe false
      Validators.validateName("a12") mustBe false
    }
  }
}
