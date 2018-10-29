package de.laubfall.apnoe.hie;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleExportsStmt;
import com.github.javaparser.ast.modules.ModuleOpensStmt;
import com.github.javaparser.ast.modules.ModuleProvidesStmt;
import com.github.javaparser.ast.modules.ModuleRequiresStmt;
import com.github.javaparser.ast.modules.ModuleUsesStmt;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.UnparsableStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.GenericVisitor;

public abstract class CallHierachyVisitorAdapter implements GenericVisitor<Void, CallHierarchyNode>
{

  private void goDeeper(Node n, CallHierarchyNode arg) {
    n.getChildNodes().forEach(cn -> cn.accept(this, arg));
  }

  @Override
  public Void visit(MethodDeclaration n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(EnclosedExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(BlockStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(CompilationUnit n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(PackageDeclaration n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(TypeParameter n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(LineComment n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(BlockComment n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ClassOrInterfaceDeclaration n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(EnumDeclaration n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(EnumConstantDeclaration n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(AnnotationDeclaration n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(AnnotationMemberDeclaration n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(FieldDeclaration n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(VariableDeclarator n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ConstructorDeclaration n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(Parameter n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(InitializerDeclaration n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(JavadocComment n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ClassOrInterfaceType n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(PrimitiveType n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ArrayType n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ArrayCreationLevel n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(IntersectionType n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(UnionType n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(VoidType n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(WildcardType n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(UnknownType n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ArrayAccessExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ArrayCreationExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ArrayInitializerExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(AssignExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(BinaryExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(CastExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ClassExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ConditionalExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(FieldAccessExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(InstanceOfExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(StringLiteralExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(IntegerLiteralExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(LongLiteralExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(CharLiteralExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(DoubleLiteralExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(BooleanLiteralExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(NullLiteralExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(NameExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ObjectCreationExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ThisExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(SuperExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(UnaryExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(VariableDeclarationExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(MarkerAnnotationExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(SingleMemberAnnotationExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(NormalAnnotationExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(MemberValuePair n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ExplicitConstructorInvocationStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(LocalClassDeclarationStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(AssertStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(LabeledStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(EmptyStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ExpressionStmt n, CallHierarchyNode arg)
  {
    n.getChildNodes().forEach(cn -> cn.accept(this, arg));
    return null;
  }

  @Override
  public Void visit(SwitchStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(SwitchEntryStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(BreakStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ReturnStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(IfStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(WhileStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ContinueStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(DoStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ForeachStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ForStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ThrowStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(SynchronizedStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(TryStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(CatchClause n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(LambdaExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(MethodReferenceExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(TypeExpr n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(NodeList n, CallHierarchyNode arg)
  {
//    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(Name n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(SimpleName n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ImportDeclaration n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ModuleDeclaration n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ModuleRequiresStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ModuleExportsStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ModuleProvidesStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ModuleUsesStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ModuleOpensStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(UnparsableStmt n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(ReceiverParameter n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

  @Override
  public Void visit(VarType n, CallHierarchyNode arg)
  {
    goDeeper(n, arg);
    return null;
  }

}
