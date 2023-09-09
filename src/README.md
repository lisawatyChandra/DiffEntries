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

