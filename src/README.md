# DiffEntries



diffEntry1={ 
    aws-firefly=[write-only, admin, read-only] 
}

diffEntry2={ 
    aws-firefly=[write-only, admin, read-only], 
    bcp-testing=[admin] 
}

diffEntry3={ 
    aws-firefly=[admin, read-only], 
    bcp-testing=[admin] 
}

diffEntry4={ 
    aws-imperium=[general-read-only], 
    aws-billingcentral-support=[admin] 
}

diffEntry5={ 
    aws-imperium=[general-read-only, general-write-only], 
    aws-billingcentral-support=[admin] 
}

=====================================================================================================


##### FIRST AUDIT: 

* add_group=[bcp-testing]
* remove_group=[]
* add_role=null
* remove_role=null


##### SECOND AUDIT: 

* add_group=[]
* remove_group=[]
* add_role=null
* remove_role=[write-only]


##### THIRD AUDIT: 

* add_group=[aws-imperium, aws-billingcentral-support]
* remove_group=[aws-firefly, bcp-testing]
* add_role=null
* remove_role=null


##### FOURTH AUDIT: 

* add_group=[]
* remove_group=[]
* add_role=[general-write-only]
* remove_role=null



FIRST AUDIT:
* You gained access to [bcp-testing] group(s) at 2023-09-09T12:35:42.683658Z



SECOND AUDIT:
* You lost access to [write-only] role(s) at 2023-09-09T12:35:42.692936Z



THIRD AUDIT:
* You gained access to [aws-imperium, aws-billingcentral-support] group(s) at 2023-09-09T12:35:42.693225Z
* You lost access to[aws-firefly, bcp-testing] group(s) at 2023-09-09T12:35:42.693289Z



FOURTH AUDIT:
* You gained access to [general-write-only] role(s) at 2023-09-09T12:35:42.693586Z
