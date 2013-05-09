#lang racket

(require "ros.rkt")

;;;;;;;;;;;;;;
;;; TESTES ;;; 
;;;;;;;;;;;;;;

(defgeneric fact (n))

(defmethod fact ((n zero?)) 0)
(defmethod fact ((n integer?)) (* n (fact (- n 1))))

(defsubtype complex? number?)
(defsubtype real? complex?)
(defsubtype rational? real?)
(defsubtype integer? rational?)
(defsubtype zero? integer?)
(defsubtype odd? integer?)
(defsubtype even? integer?)
(defsubtype zero? even?)

(defsubtype string? char?)

(defsubtype zero? string?)

(defsubtype char? number?)

(defsubtype odd? char?)

(defsubtype integer? string?)

(defgeneric add (x y))

(defmethod add ((x zero?) (y zero?))
  (display "zero?"))

(defmethod add ((x zero?) (y zero?))
  (display "zero?-redefined"))

(defmethod add ((x integer?) (y integer?))
  (display "integer?"))

(defmethod add ((x integer?) (y zero?))
  (display "integer-zero?"))

(defmethod add ((x even?) (y even?))
  (display "even?"))

(defmethod add ((x even?) (y integer?))
  (display "even-integer?"))

(defmethod add ((x complex?) (y complex?))
  (display "complex?"))

(defmethod add ((x real?) (y real?))
  (display "real?"))

(defmethod add ((x string?) (y string?))
  (string-append x y))

(defmethod add ((x string?) (y string?))
  (string-append y x))