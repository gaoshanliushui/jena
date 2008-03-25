/* Generated By:JavaCC: Do not edit this line. SPARQLUpdateParserConstants.java */
/*
 * (c) Copyright 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 */

package com.hp.hpl.jena.sparql.modify.lang.parser ;

public interface SPARQLUpdateParserConstants {

  int EOF = 0;
  int MODIFY = 1;
  int INSERT = 2;
  int DELETE = 3;
  int ADD = 4;
  int REMOVE = 5;
  int LOAD = 6;
  int CLEAR = 7;
  int CREATE = 8;
  int SILENT = 9;
  int DROP = 10;
  int INTO = 11;
  int BEGIN = 12;
  int COMMIT = 13;
  int ABORT = 14;
  int WS = 20;
  int SINGLE_LINE_COMMENT = 21;
  int IRIref = 22;
  int PNAME_NS = 23;
  int PNAME_LN = 24;
  int BLANK_NODE_LABEL = 25;
  int VAR1 = 26;
  int VAR2 = 27;
  int LANGTAG = 28;
  int A2Z = 29;
  int A2ZN = 30;
  int KW_A = 31;
  int BASE = 32;
  int PREFIX = 33;
  int ASC = 34;
  int DESC = 35;
  int FROM = 36;
  int WHERE = 37;
  int GRAPH = 38;
  int OPTIONAL = 39;
  int UNION = 40;
  int FILTER = 41;
  int BOUND = 42;
  int STR = 43;
  int DTYPE = 44;
  int LANG = 45;
  int LANGMATCHES = 46;
  int IS_URI = 47;
  int IS_IRI = 48;
  int IS_BLANK = 49;
  int IS_LITERAL = 50;
  int REGEX = 51;
  int SAME_TERM = 52;
  int TRUE = 53;
  int FALSE = 54;
  int INTEGER = 55;
  int DECIMAL = 56;
  int DOUBLE = 57;
  int INTEGER_POSITIVE = 58;
  int DECIMAL_POSITIVE = 59;
  int DOUBLE_POSITIVE = 60;
  int INTEGER_NEGATIVE = 61;
  int DECIMAL_NEGATIVE = 62;
  int DOUBLE_NEGATIVE = 63;
  int EXPONENT = 64;
  int QUOTE_3D = 65;
  int QUOTE_3S = 66;
  int ECHAR = 67;
  int STRING_LITERAL1 = 68;
  int STRING_LITERAL2 = 69;
  int STRING_LITERAL_LONG1 = 70;
  int STRING_LITERAL_LONG2 = 71;
  int DIGITS = 72;
  int LPAREN = 73;
  int RPAREN = 74;
  int NIL = 75;
  int LBRACE = 76;
  int RBRACE = 77;
  int LBRACKET = 78;
  int RBRACKET = 79;
  int ANON = 80;
  int SEMICOLON = 81;
  int COMMA = 82;
  int DOT = 83;
  int EQ = 84;
  int NE = 85;
  int GT = 86;
  int LT = 87;
  int LE = 88;
  int GE = 89;
  int BANG = 90;
  int TILDE = 91;
  int COLON = 92;
  int SC_OR = 93;
  int SC_AND = 94;
  int PLUS = 95;
  int MINUS = 96;
  int STAR = 97;
  int SLASH = 98;
  int DATATYPE = 99;
  int AT = 100;
  int PN_CHARS_BASE = 101;
  int PN_CHARS_U = 102;
  int PN_CHARS = 103;
  int PN_PREFIX = 104;
  int PN_LOCAL = 105;
  int VARNAME = 106;
  int UNKNOWN = 107;

  int DEFAULT = 0;

  String[] tokenImage = {
    "<EOF>",
    "\"modify\"",
    "\"insert\"",
    "\"delete\"",
    "\"add\"",
    "\"remove\"",
    "\"load\"",
    "\"clear\"",
    "\"create\"",
    "\"silent\"",
    "\"drop\"",
    "\"into\"",
    "\"begin\"",
    "\"commit\"",
    "\"abort\"",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "<WS>",
    "<SINGLE_LINE_COMMENT>",
    "<IRIref>",
    "<PNAME_NS>",
    "<PNAME_LN>",
    "<BLANK_NODE_LABEL>",
    "<VAR1>",
    "<VAR2>",
    "<LANGTAG>",
    "<A2Z>",
    "<A2ZN>",
    "\"a\"",
    "\"base\"",
    "\"prefix\"",
    "\"asc\"",
    "\"desc\"",
    "\"from\"",
    "\"where\"",
    "\"graph\"",
    "\"optional\"",
    "\"union\"",
    "\"filter\"",
    "\"bound\"",
    "\"str\"",
    "\"datatype\"",
    "\"lang\"",
    "\"langmatches\"",
    "\"isURI\"",
    "\"isIRI\"",
    "\"isBlank\"",
    "\"isLiteral\"",
    "\"regex\"",
    "\"sameTerm\"",
    "\"true\"",
    "\"false\"",
    "<INTEGER>",
    "<DECIMAL>",
    "<DOUBLE>",
    "<INTEGER_POSITIVE>",
    "<DECIMAL_POSITIVE>",
    "<DOUBLE_POSITIVE>",
    "<INTEGER_NEGATIVE>",
    "<DECIMAL_NEGATIVE>",
    "<DOUBLE_NEGATIVE>",
    "<EXPONENT>",
    "\"\\\"\\\"\\\"\"",
    "\"\\\'\\\'\\\'\"",
    "<ECHAR>",
    "<STRING_LITERAL1>",
    "<STRING_LITERAL2>",
    "<STRING_LITERAL_LONG1>",
    "<STRING_LITERAL_LONG2>",
    "<DIGITS>",
    "\"(\"",
    "\")\"",
    "<NIL>",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "<ANON>",
    "\";\"",
    "\",\"",
    "\".\"",
    "\"=\"",
    "\"!=\"",
    "\">\"",
    "\"<\"",
    "\"<=\"",
    "\">=\"",
    "\"!\"",
    "\"~\"",
    "\":\"",
    "\"||\"",
    "\"&&\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"^^\"",
    "\"@\"",
    "<PN_CHARS_BASE>",
    "<PN_CHARS_U>",
    "<PN_CHARS>",
    "<PN_PREFIX>",
    "<PN_LOCAL>",
    "<VARNAME>",
    "<UNKNOWN>",
  };

}
