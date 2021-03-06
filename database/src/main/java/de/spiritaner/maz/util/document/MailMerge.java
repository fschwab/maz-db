package de.spiritaner.maz.util.document;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.codehaus.plexus.util.StringUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

import java.io.*;
import java.util.List;

/*
	Copyright (c) 2015, Dominik Stadler
	All rights reserved.
	https://github.com/centic9/poi-mail-merge

	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions are met:

	* Redistributions of source code must retain the above copyright notice, this
	  list of conditions and the following disclaimer.

	* Redistributions in binary form must reproduce the above copyright notice,
	  this list of conditions and the following disclaimer in the documentation
	  and/or other materials provided with the distribution.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
	AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
	IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
	DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
	FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
	DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
	SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
	CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
	OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
	OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

	! ----------------------------------------------------------- !
	! The source code below was modified to fit the current needs !
	! ----------------------------------------------------------- !
 */
public class MailMerge {

	final static Logger logger = Logger.getLogger(MailMerge.class);

	public static void merge(final File wordTemplate, final File outputFile, final MailMergeData data) throws Exception {
		logger.info("Merging data into " + outputFile + " with template " + wordTemplate);

		// now open the word file and apply the changes
		try (InputStream is = new FileInputStream(wordTemplate)) {
			try (XWPFDocument doc = new XWPFDocument(is)) {
				// apply the lines and concatenate the results into the document
				applyLines(data, doc);

				logger.info("Writing overall result to " + outputFile);
				try (OutputStream out = new FileOutputStream(outputFile)) {
					doc.write(out);
				}
			}
		}
	}

	private static void applyLines(MailMergeData dataIn, XWPFDocument doc) throws XmlException, IOException {
		CTBody body = doc.getDocument().getBody();

		// read the current full Body text
		String srcString = body.xmlText();

		// apply the replacements line-by-line
		boolean first = true;
		List<String> headers = dataIn.getHeaders();
		for(List<String> data : dataIn.getData()) {
			logger.info("Applying to template: " + data);

			String replaced = srcString;
			for(int fieldNr = 0;fieldNr < headers.size();fieldNr++) {
				String header = headers.get(fieldNr);
				String value = data.get(fieldNr);

				// ignore columns without headers as we cannot match them
				if(header == null) {
					continue;
				}

				// use empty string for data-cells that have no value
				if(value == null) {
					value = "";
				}

				replaced = replaced.replace("${" + header + "}", value);
			}

			// check for missed replacements or formatting which interferes
			if(replaced.contains("${")) {
				logger.warn("Still found template-marker after doing replacement: " +
						  StringUtils.abbreviate(StringUtils.substring(replaced, replaced.indexOf("${")), 200));
			}

			appendBody(body, replaced, first);

			first = false;
		}
	}

	private static void appendBody(CTBody src, String append, boolean first) throws XmlException {
		XmlOptions optionsOuter = new XmlOptions();
		optionsOuter.setSaveOuter();
		String srcString = src.xmlText();
		String prefix = srcString.substring(0,srcString.indexOf(">")+1);

		final String mainPart;
		// exclude template itself in first appending
		if(first) {
			mainPart = "";
		} else {
			mainPart = srcString.substring(srcString.indexOf(">")+1,srcString.lastIndexOf("<"));
		}

		String suffix = srcString.substring( srcString.lastIndexOf("<") );
		String addPart = append.substring(append.indexOf(">") + 1, append.lastIndexOf("<"));
		logger.info(prefix+mainPart+addPart+suffix);
		CTBody makeBody = CTBody.Factory.parse(prefix+mainPart+addPart+suffix);
		src.set(makeBody);
	}
}