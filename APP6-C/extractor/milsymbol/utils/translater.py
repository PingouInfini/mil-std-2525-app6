import csv

from deep_translator import GoogleTranslator


def translate_to_french(text):
    translated_word = GoogleTranslator(source='en', target='fr').translate(text)
    return translated_word


def translate_to_french_from_csv(text):
    with open('rawdata/traduction_EN_FR.csv', 'r', newline='') as csvfile:
        csv_reader = csv.reader(csvfile, delimiter=';')
        for row in csv_reader:
            if row and row[0] == text:
                return row[1]

    return text
