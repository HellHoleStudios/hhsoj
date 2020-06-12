'''
the argument to call this 
core.py <tl> <ml> <opCodeLen> <opCodes> <opFileLen> <opFiles> <codeLen> <code>
'''


try:
    import lorun 
    import os
    import sys
    stderr=sys.stderr
except Exception as e: 
    print("Cannot import classes:",e,file=stderr)
    exit(101)

print('import classes success!',file=stderr)

try:
    tl=int(sys.argv[1])
    ml=int(sys.argv[2])
    opCode=[]
    opFile=dict()
    cmd=[]

    pointer=3
    rng=int(sys.argv[pointer])
    for i in range(rng):
        pointer+=1
        opCode.append(int(sys.argv[pointer]))

    pointer+=1
    rng=int(sys.argv[pointer])
    for i in range(rng):
        pointer+=1
        opFile[sys.argv[pointer]]=524288

    pointer+=1
    rng=int(sys.argv[pointer])
    for i in range(rng):
        pointer+=1
        cmd.append(sys.argv[pointer])

except Exception as e:
    print("Failed to read system argument:",e,file=stderr)
    exit(102)

print("Sys arg init ok:",tl,ml,opCode,opFile,cmd,file=stderr)

try:
    fin = open("in.txt")
    ftemp = open('out.txt', 'w')

    runcfg = {
        'args':cmd,
        'fd_in':fin.fileno(),
        'fd_out':ftemp.fileno(),
        'timelimit':tl, #in MS
        'memorylimit':ml, #in KB
    }
    runcfg['trace'] = True
    runcfg['calls'] = opCode # system calls that could be used by testing programs
    runcfg['files'] = opFile # open flag permitted (value is the flags of open)

    rst = lorun.run(runcfg)
    fin.close()
    ftemp.close()

    print(rst,file=stderr)

except Exception as e:
    print("Cannot run lorun:",e,file=stderr)
    exit(103)

print("Checking code",file=stderr)

try:
    if rst['result']==5:
        if 're_call' in rst:
            print('RF',rst['timeused'],rst['memoryused'],"Illegal Call Id:"+str(rst['re_call']),sep='\n')
        elif 're_file' in rst:
            print('RF',rst['timeused'],rst['memoryused'],"Illegal Read Attempt:"+rst['re_file'],sep='\n')
        else:
            print('RE',rst['timeused'],rst['memoryused'],rst['re_signum'],sep='\n')
    elif rst['result']==0:
        print('OK',rst['timeused'],rst['memoryused'],sep='\n')
    elif rst['result']==2:
        print('TLE',rst['timeused'],rst['memoryused'],sep='\n')
    elif rst['result']==3:
        print('MLE',rst['timeused'],rst['memoryused'],sep='\n')
    else:
        print('UKE',rst['timeused'],rst['memoryused'],rst,sep='\n')
except Exception as e:
    print("Cannot check code:",e,file=stderr)
    exit(104)