from deep_translator import GoogleTranslator


def translate_to_french(text):
    translated_word = GoogleTranslator(source='en', target='fr').translate(text)
    return translated_word
