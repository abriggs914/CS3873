#lang racket
(define chr integer->char)
(define ord char->integer)
 
(define (encrypt msg key)
  (define cleaned
    (list->string
     (for/list ([c (string-upcase msg)] 
                #:when (char-alphabetic? c)) c)))
  (list->string
   (for/list ([c cleaned] [k (in-cycle key)])
     (chr (+ (modulo (+ (ord c) (ord k)) 26) (ord #\A))))))
 
(define (decrypt msg key)
  (list->string
   (for/list ([c msg] [k (in-cycle key)])
     (chr (+ (modulo (- (ord c) (ord k)) 26) (ord #\A))))))
 
(module+ test
 (require rackunit)
 (check-equal? (decrypt (encrypt "Beware the Jabberwock, my son! The jaws that bite, the claws that catch!"
                  "VIGENERECIPHER")
         "VIGENERECIPHER")
               "BEWARETHEJABBERWOCKMYSONTHEJAWSTHATBITETHECLAWSTHATCATCH")

  (check-equal? (decrypt "NXRUOYLIWAKWMXLIPFAMNYXRAPYTFQFXATLPYZSLJGNXRILLOCFIPNLXLSXLDPMAPEAAQBVDORLDQFAQNUYJIZNHFJAKRHOMPVUAPZUQYNXDFXGRLDPIYIKMRAGNXPYXNPOMAZFIQBIALOQAAIJFLPMRAHYPAFLWLPKQMFFLDQFAQNOPQIYJYXJSLYWXWMFEHQRUOLUMDOONUQRIKEHXLDFARXDTFLSQPUZRILLIKZRXLTLLHFJLPQCEMOIQFLPSZFOLJIKEAZAOOBIKETLFIJBIALOQAOKQIPROKJYLDBLMKPYNAZLXAKYMAOBSEYNAQAIQOPFORJDHLOTFOTROZMNPRRRATSYRFMUPNRXATFAAIRHFLGPGNTMOAUOOIPISMYGNDZLXAKPKIQFIKEMXQOKPYXLDPMFLPTEUIQFTEMRLSGEGNPRRRATFMNFLSXLIQYTFMNXLDQFAQKOPRIJNOORAKROCYLIWORRHCSLXATFTIQWGVK"
                         "VIGENERE")
                "INGODWETRUSTALLOTHERSWEVIRUSSCAN")
  
  (check-equal? (encrypt "INGODWETRUSTALLOTHERSWEVIRUSSCAN"
                  "VIGENERE")
               "DVMSQAVXMCYXNPCSOPKVFAVZDZAWFGRR")
 )