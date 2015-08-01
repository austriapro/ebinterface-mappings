![ebInterface Logo](https://github.com/pliegl/ebinterface/blob/master/site/images/logo.jpg?raw=true "ebInterface e-Invoice standard")

#ebInterface Mappings

This project collects various document mappings, allowing to transform a given ebInterface instance to another document format and vice versa.

The document mappings are provided on a technical and on a executable level. Technical mappings describe the relationship between ebInterface and a given target format using regular spread sheets. Executable mappings are provided in Java and allow to map a given ebInterface XML instance to a target format (and vice versa, if provided).

The resulting mappings may become parts of other third-party applications.

Executable mappings are integrated in [ebinterface-web](https://github.com/austriapro/ebinterface-web) as well, where they may be easily used by simply uploading an ebInterface XML instance document. 

##Supported mappings

Currently, the following mappings are planned.

 * ZUGFeRD (Germany)
 * UBL 2.1 (International)
 
 * e-SLOG (Slovenia)
 * FatturaPA (Italy)
 * GS1 XML (International)
 	
##Code style

For this project the Google code style is used. Please [download the code style file](https://code.google.com/p/google-styleguide/source/browse/trunk/intellij-java-google-style.xml) and set it in your IDE, before you commit to the repository. 

:green_heart: Pull requests are greatly appreciated and welcomed.
