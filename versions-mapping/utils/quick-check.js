import { convertLetterSidc2NumberSidc } from './convert-symbology/lib';

import { convertNumberSidc2LetterSidc } from "@orbat-mapper/convert-symbology";

// Déclaration de la constante sicd_in
const sidc_in = "S*G*USAST-";
const { sidc, match, success } = convertLetterSidc2NumberSidc(sidc_in);
console.log(sidc);
console.log(match);
console.log(success);
