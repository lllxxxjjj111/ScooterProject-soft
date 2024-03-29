RS EQU P1.5 ; circuit dependent EQUs
RW EQU P1.6 ; EQU is an assembler directive
EN EQU P1.7 ; so these are not 8051 instructions
LCDDB EQU P0
LCDBF EQU P0.7
C1 EQU P0.0
C2 EQU P0.1
C3 EQU P0.2
C4 EQU P0.3
RO1 EQU P0.4
RO2 EQU P0.5
RO3 EQU P0.6
RO4 EQU P0.7
STDB EQU P2
ST EQU 40H
COT1 EQU 41H
COT2 EQU 42H
COT3 EQU 43H
COT4 EQU 44H
BEEPFLAG EQU 45H

ORG 0000H
LJMP MAIN
ORG 000BH
LJMP T0ISR
ORG 002Bh
LJMP T2ISR
ORG 0030H
MAIN:
MOV TMOD,#21H
ACALL UART_INIT
MOV A, #38h ; 2 lines, 5x7, 8-bit
ACALL COMNWRT ; send command
MOV A, #0Eh ; display ON
ACALL COMNWRT
MOV A, #06h ; addr auto-increment
ACALL COMNWRT
MOV A, #01h ; clear LCD
ACALL COMNWRT
MOV A, #80h ; set DDRAM=0
ACALL COMNWRT
MOV DPTR,#HELLO
ACALL MESSAGE

LOOP1:
MOV P0,#0F0H
MOV A,P0
CJNE A,#0F0H,SCAN1
SJMP LOOP1
SCAN1:ACALL DECODE
ACALL PRINT
JZ LOOP2
SJMP LOOP1

LOOP2:
MOV P0,#0F0H
MOV A,P0
CJNE A,#0F0H,SCAN2
JNB ET0,TIMEOUT
SJMP LOOP2
SCAN2:ACALL DECODE
ACALL SELECT
CJNE A,#'!',LOOP2
MOV DPTR,#TAKESUCS
ACALL MESSAGE
ACALL BIGDELAY
SJMP MAIN
TIMEOUT:MOV A,#'?'
ACALL UART_SEND
MOV DPTR,#TAKEFAIL
ACALL MESSAGE
ACALL BIGDELAY
SJMP MAIN

DECODE:
ACALL DELAY
MOV P0,#0F0H
MOV A,P0
SUBB A,#0F0H
JZ LOOP1
MOV P0,#0FEH
JNB RO1,C1R1
JNB RO2,C1R2
JNB RO3,C1R3
JNB RO4,C1R4
MOV P0,#0FDH
JNB RO1,C2R1
JNB RO2,C2R2
JNB RO3,C2R3
JNB RO4,C2R4
MOV P0,#0FBH
JNB RO1,C3R1
JNB RO2,C3R2
JNB RO3,C3R3
JNB RO4,C3R4
MOV P0,#0F7H
JNB RO1,C4R1
JNB RO2,C4R2
JNB RO3,C4R3
JNB RO4,C4R4
C1R1:MOV A,#0
SJMP SCANEND
C2R1:MOV A,#1
SJMP SCANEND
C3R1:MOV A,#2
SJMP SCANEND
C4R1:MOV A,#3
SJMP SCANEND
C1R2:MOV A,#4
SJMP SCANEND
C2R2:MOV A,#5
SJMP SCANEND
C3R2:MOV A,#6
SJMP SCANEND
C4R2:MOV A,#7
SJMP SCANEND
C1R3:MOV A,#8
SJMP SCANEND
C2R3:MOV A,#9
SJMP SCANEND
C3R3:MOV A,#10
SJMP SCANEND
C4R3:MOV A,#11
SJMP SCANEND
C1R4:MOV A,#12
SJMP SCANEND
C2R4:MOV A,#13
SJMP SCANEND
C3R4:MOV A,#14
SJMP SCANEND
C4R4:MOV A,#15
SJMP SCANEND
SCANEND:
MOV P0,#0F0H
RELEASE:
MOV R0,P0
CJNE R0,#0F0H,RELEASE
ACALL DELAY
RET

PRINT:
CLR C
CJNE A,#10,CMPTEN
CMPTEN:
JNC DEL
ADD A,#'0'
ACALL UART_SEND
ACALL DATAWRT
RET
DEL:
MOV R2,A
SUBB A,#10
JNZ DELEND
MOV A,R2
ACALL UART_SEND
MOV A,#10H
ACALL COMNWRT
MOV A,#20H
ACALL DATAWRT
MOV A,#10H
ACALL COMNWRT
RET
DELEND:
MOV A,R2
SUBB A,#11
JNZ COMTEND
MOV A,R2
ACALL UART_SEND
ACALL UART_RESV
ACALL REACT
JZ COMTEND
POP ACC
POP ACC
LJMP MAIN
COMTEND:
RET

REACT:
CJNE A,#12,IDERROR
MOV DPTR,#SYTAXERROR
ACALL MESSAGE
ACALL BIGDELAY
MOV A,#1
RET
IDERROR:CJNE A,#13,NOSLT
MOV DPTR,#NOID
ACALL MESSAGE
ACALL BIGDELAY
MOV A,#1
RET
NOSLT:CJNE A,#14,NOSCT
MOV DPTR,#NOSLOT
ACALL MESSAGE
ACALL BIGDELAY
POP ACC
POP ACC
LJMP MAIN
NOSCT:CJNE A,#15,FINE
MOV DPTR,#NOSCOOTER
ACALL MESSAGE
ACALL BIGDELAY
MOV A,#1
RET
FINE:CJNE A,#16,ALLOWBORW
MOV DPTR,#BAN
ACALL MESSAGE
ACALL BIGDELAY
MOV A,#1
RET
ALLOWBORW:CJNE A,#17,ALLOWRET
MOV DPTR,#OPENTAKE
ACALL MESSAGE
ACALL UART_RESV
ACALL FLASH
MOV A,#0
RET
ALLOWRET:MOV DPTR,#OPENRET
ACALL MESSAGE
ACALL UART_RESV
ACALL FLASH
MOV A,#0
RET

SELECT:
PUSH 4
MOV R4,A
MOV A,ST
JNB ACC.0,SLOT0
JNB ACC.1,SLOT1
JNB ACC.2,SLOT2
JNB ACC.3,SLOT3
JNB ACC.4,SLOT4
JNB ACC.5,SLOT5
JNB ACC.6,SLOT6
JNB ACC.7,SLOT7
SLOT0:MOV A,#0
SJMP SLTEND
SLOT1:MOV A,#1
SJMP SLTEND
SLOT2:MOV A,#2
SJMP SLTEND
SLOT3:MOV A,#3
SJMP SLTEND
SLOT4:MOV A,#4
SJMP SLTEND
SLOT5:MOV A,#5
SJMP SLTEND
SLOT6:MOV A,#6
SJMP SLTEND
SLOT7:MOV A,#7
SLTEND:CJNE A,04H,SELECTEND
CLR ET0
CLR EA
MOV STDB,#0FFH
MOV A,#'!'	;confirm the slot
ACALL UART_SEND
SELECTEND:POP 4
RET

MESSAGE:
PUSH ACC
PUSH 5
MOV A, #01h ; clear LCD
ACALL COMNWRT
MOV A, #80h ; set DDRAM=0
ACALL COMNWRT
CLR A
MOV R5,A
MESSMOV:MOVC A,@A+DPTR
JZ MESSEND
ACALL DATAWRT
MOV A,R5
INC A
MOV R5,A
CJNE A,#16,MESSMOV
MOV A,#0C0H
ACALL COMNWRT
MOV A,R5
SJMP MESSMOV
MESSEND:MOV A,#0C0H
ACALL COMNWRT
POP 5
POP ACC
RET

HELLO:DB 'INPUT YOUR ID:',0
SYTAXERROR:DB 'ID MUST 9 DIGITS',0
NOID:DB 'ID NOT EXISTS',0
OPENTAKE:DB 'SLOT OPEN PLEASE TAKE',0
OPENRET:DB 'SLOT OPEN PLEASE RETURN',0
TAKESUCS:DB 'SUCCESS',0
TAKEFAIL:DB 'TIME OUT',0
NOSLOT:DB 'NO FREE SLOT',0
NOSCOOTER:DB 'NO SCOOTER',0
BAN:DB 'ID BANNED,PLEASE CHECK FINE',0

COMNWRT:
ACALL READY
MOV LCDDB, A
CLR RS
CLR RW
SETB EN
NOP
CLR EN
MOV LCDDB,#0FFH
RET

DATAWRT:
ACALL READY
MOV LCDDB, A
SETB RS
CLR RW
SETB EN
NOP
CLR EN
MOV LCDDB,#0FFH
RET

READY:
PUSH 7
MOV LCDDB, #0FFh
MOV R7, #255
CLR RS
SETB RW
SETB EN
POLLBF:
JNB LCDBF, POLLOK
DJNZ R7, POLLBF
POLLOK:
CLR EN
POP 7
RET

UART_INIT:
MOV TH1,#0FDH
SETB TR1
MOV SCON,#50H
RET

UART_SEND:
CLR TI
MOV SBUF,A
JNB TI,$
RET

UART_RESV:
JNB RI,$
MOV A,SBUF
CLR RI
RET

DELAY:
PUSH 0
PUSH 2
MOV R0,#10
RR1:MOV R2,#100
DJNZ R2,$
DJNZ R0,RR1
POP 2
POP 0
RET

BIGDELAY:
PUSH 6
PUSH 7
MOV R6,#10
RR7:MOV R7,#100
REDELAY:ACALL DELAY
DJNZ R7,REDELAY
DJNZ R6,RR7
POP 7
POP 6
RET

FLASH:
MOV COT1,#0
MOV COT2,#12
MOV ST,A
SETB ET0
SETB EA
SETB TF0
RET

BEEP: 
MOV COT3,#0FFH
MOV COT4,#2
MOV BEEPFLAG,#0
MOV T2MOD, #0h ; timer 0 mode 1
SETB ET2 ; enable timer 0 INT
SETB EA
SETB TF2
RET

T0ISR: CLR TF0 ; clear TF0
MOV TH0, #04Bh ; setup timer 0
MOV TL0, #0FDh
SETB TR0 ; start timer
PUSH ACC
MOV A,COT1
JB ACC.2,END1
JB ACC.1,ODD
MOV STDB,ST
SJMP END1
ODD:MOV STDB,#0FFH
END1:INC A
MOV COT1,A
CJNE A,#100,END2
MOV COT1,#0
ACALL BEEP
DJNZ COT2,END2
MOV COT2,#6
CLR ET0
CLR EA
MOV STDB,#0FFH
END2:POP ACC
RETI

T2ISR:
PUSH ACC
CLR TF2
MOV A,BEEPFLAG
CJNE A,#0,BPDL
MOV TH2,#0FFH
MOV TL2,#23H
SETB TR2
CPL P1.1
DJNZ COT3,T0ISREND
DJNZ COT4,T0ISREND
MOV BEEPFLAG,#1
MOV COT3,#0FFH
MOV COT4,#3
BPDL:
MOV TH2,#0FEH
MOV TL2,#47H
SETB TR2
CPL P1.1
DJNZ COT3,T0ISREND
DJNZ COT4,T0ISREND
CLR ET2
T0ISREND:POP ACC
RETI