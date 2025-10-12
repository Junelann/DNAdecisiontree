library(randomForest)
library(caret)
library(e1071)
library(pROC)
library(ggplot2)
setwd("/Users/mac/Desktop/Data")
cpg <- read.table('DFR.txt', sep = '\t', row.names = 1, header = TRUE, fill = TRUE)
group <- read.table('group.txt', sep = '\t', row.names = 1, header = TRUE, fill = TRUE)
cpg <- data.frame(t(cpg))
cpg_group <- cbind(cpg, group)

set.seed(123)
cpg_group$group=as.factor(cpg_group$group)
cpg_group.forest <- randomForest(group ~ ., data = cpg_group, importance = TRUE)
cpg_group.forest
plot(cpg_group.forest, main = "OOB error rate versus number of trees")

set.seed(123)
importance_otu <- data.frame(importance(cpg_group.forest))
importance_otu <- importance_otu[order(importance_otu$MeanDecreaseAccuracy, decreasing = TRUE), ]
otu_select <- rownames(importance_otu)[1:8]
CPG_TOP <- cpg_group[ ,c(otu_select)]
write.csv(CPG_TOP,"8CpGprofiles.csv")

cpg_6 <- read.table('6CpGprofiles-raw.txt', sep = '\t', row.names = 1, header = TRUE, fill = TRUE)
cpg_6 <- data.frame(t(cpg_6))

cpg_6[cpg_6> 0.6] <- 0.65
cpg_6[cpg_6>= 0.2 & cpg_6 <= 0.6] <- 0.45
cpg_6[cpg_6< 0.2] <- 0.25
cpg6_group <- cbind(cpg_6, group)
write.csv(cpg6_group,"6CpGprofiles-encoded label.csv")

set.seed(123)
cpg6_group$group=as.factor(cpg6_group$group)
cpg6_group.forest <- randomForest(group ~ ., data = cpg6_group,importance = TRUE)
cpg6_group.forest
plot(cpg6_group.forest, main = "OOB error rate versus number of trees")
which.min(cpg6_group.forest$err.rate[,1])


customRF <- list(type = "Classification", library = "randomForest", loop = NULL)
customRF$parameters <- data.frame(parameter = c("mtry", "ntree"), class = rep("numeric", 2), label = c("mtry", "ntree"))
customRF$grid <- function(x, y, len = NULL, search = "grid") {}
customRF$fit <- function(x, y, wts, param, lev, last, weights, classProbs, ...) {
  randomForest(x, y, mtry = param$mtry, ntree=param$ntree, ...)
}
customRF$predict <- function(modelFit, newdata, preProc = NULL, submodels = NULL)
  predict(modelFit, newdata)
customRF$prob <- function(modelFit, newdata, preProc = NULL, submodels = NULL)
  predict(modelFit, newdata, type = "prob")
customRF$sort <- function(x) x[order(x[,1]),]
customRF$levels <- function(x) x$classes
control <- trainControl(method="repeatedcv", number=10, repeats=3)
tunegrid <- expand.grid(.mtry=c(1:8), .ntree=c(7,9,11,13,15,17,19,21,27,23,25))
set.seed(123)
custom <- train(group~., data = cpg6_group, method=customRF, tuneGrid=tunegrid, trControl=control, metric="Accuracy")
print(custom)
plot(custom)
set.seed(123)
cpg6_group$group=as.factor(cpg6_group$group)
cpg6_group.forest <- randomForest(group ~ ., data = cpg6_group, 
                                  ntree=13, mtry=1,importance = TRUE)
cpg6_group.forest
plot(cpg6_group.forest, main = "OOB error rate versus number of trees")
which.min(cpg6_group.forest$err.rate[,1])

S=as.data.frame(predict(cpg6_group.forest, newdata=cpg6_group, predict.all=TRUE))
write.csv(S,"prediction_result.csv")

library(pmml)
cpg6_group.forest_pmml <- pmml(cpg6_group.forest)
save_pmml(cpg6_group.forest_pmml,"cpg6_group.forest_pmml.pmml")
p1 <- predict(cpg6_group.forest, cpg6_group)
confusionMatrix(p1, cpg6_group$groups)
p2 <- predict(cpg6_group.forest, otu_test_top30)
otu_test_top30$groups=as.factor(otu_test_top30$groups)
confusionMatrix(p2, otu_test_top30$groups)
train_predict <- predict(cpg_train.forest, cpg_train)
compare_train <- table(train_predict, cpg_train$group)
compare_train
test_predict <- predict(cpg_train.forest, cpg_test)
compare_test <- table(cpg_test$group, test_predict, dnn = c('Actual', 'Predicted'))
compare_test