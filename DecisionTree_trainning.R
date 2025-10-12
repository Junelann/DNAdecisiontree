setwd("/Users/mac/Desktop/Data")
targets <- as.matrix(read.table('GSE218549_GPL21145_matrix_processed.txt', sep = '\t',  header = TRUE, row.names = 1))
library(impute) 
beta=impute.knn(targets)
betaData=beta$data
betaData=betaData+0.00001
sum(is.na(betaData))

group2 <- read.table('group2class.txt',sep = '\t')
group2 <- as.character(group2$V1)

library(limma)
library(edgeR)
library(statmod)
design <- model.matrix(~ 0 + factor(group2))
colnames(design) <- levels(factor(group2))
rownames(design) <- colnames(betaData)
contrasts <- paste0("thymoma", "-", "normal")
contrast.matrix <- makeContrasts(contrasts = contrasts, levels = design)
fit <- lmFit(betaData, design)
fit2 <- contrasts.fit(fit, contrast.matrix) 
fit2 <- eBayes(fit2,0.01)
tempOutput = topTable(fit2, adjust="fdr", sort.by="B",coef=1, number=nrow(betaData))
nrDEG = na.omit(tempOutput) 
head(nrDEG)
write.csv(nrDEG,"limma_notrend.results.csv",quote = F)
significant_DMG<-nrDEG[nrDEG$adj.P.Val<0.1, ]
DMGmatrix<-targets[which(rownames(targets)%in%rownames(significant_DMG)),]
dim(DMGmatrix)
write.csv(DMGmatrix,"DMG-matrix.csv",quote = F)

# 2. Train model using DMGs data
setwd("/Users/mac/Desktop/Data")
DMGmatrix <- read.csv('DMG-matrix.csv', row.names = 1, header = TRUE, fill = TRUE)
DMGmatrix[is.na(DMGmatrix)] <- 0
group1 <- read.table('group.txt', sep = '\t', row.names = 1, header = TRUE, fill = TRUE)
DMGmatrix <- data.frame(t(DMGmatrix))
DMGmatrix[DMGmatrix>= 0.6] = "High"
DMGmatrix[DMGmatrix> 0.2 & DMGmatrix < 0.6] = "Medium" 
DMGmatrix[DMGmatrix<= 0.2] = "Low"
traindata<- cbind(DMGmatrix, group1)
w=which(traindata$group=="normal_thymus")
traindata = traindata[-w,]
sum(is.na(traindata))
xx=colnames(traindata)
traindata[xx]<-lapply(traindata[xx],factor)

FeatureCpG_Matrix=subset(traindata, select=c(cg02906557,cg18121066,cg26007358,cg22795586,cg15252509))
write.csv(FeatureCpG_Matrix,"7_Feature_CpG_Matrix.csv")


library(rpart)
library(rpart.plot)
library(rattle)
library(pROC)
library(ggplot2)

set.seed(0)
sub<-sample(1:nrow(traindata),nrow(traindata)*0.85)
train<-traindata[sub,]
test<-traindata[-sub,]
model <- rpart(train$group~.,data = train,method = "class")

rpart.plot(model,
           branch.lty=8,
           shadow.col="gray")
p_train <- predict(model,type = 'class')
write.csv(p_train,"result_p_train.csv",quote = F)
c1=table(p_train,train$group,dnn = c("Predicted","Actual"))
accuracy_Train <- sum(diag(c1)) / sum(c1)
print(paste('Accuracy for train', accuracy_Train))
p_test<-predict(model,newdata = test,type='class')
write.csv(p_test,"result_p_test.csv",quote = F)
c2=table(test$group,p_test,dnn = c("Actual","Predicted"))
write.csv(c2,"confusionMatrix-p_test.csv",quote = F)
accuracy_Test <- sum(diag(c2)) / sum(c2)
print(paste('Accuracy for test', accuracy_Test))

p1 <- predict(model, train)
train$group=as.factor(train$group)
confusionMatrix(p1, train$group)
p2 <- predict(model, otu_test_top30)
otu_test_top30$groups=as.factor(otu_test_top30$groups)
confusionMatrix(p2, otu_test_top30$groups)
p1 <- predict(model, train, type = "prob")
test$group=as.factor(test$group)
set.seed(123)
roc <- roc(test$group,p1[,1],ci=T,auc = T)