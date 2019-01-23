package pgssoft.com.githubreposlist.data

import pgssoft.com.githubreposlist.data.db.Repository


fun getTestReposList(): List<Repository> = mutableListOf<Repository>().apply {
    add(Repository(1, "RepoName1", "Description1", "c++", "1544518849", "1544521849", "", false    ))
    add(Repository(2, "RepoName2", "anotherDescription", "java", "1534518849", "1539521849", "comment", false))
    add(Repository(3, "RepoName3", "3description", "kotlin", "1534418849", "1439521449", "random comment", false))
}
