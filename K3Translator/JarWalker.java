/* JarWalker - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

public class JarWalker
{
    public static int BUFFER_SIZE = 10240;
    public static String cAltSymbols
    = "\u00bf\u00c0\u00c1\u00c2\u00c3\u00c4\u00c5\u00c6\u00c7\u00c8\u00c9\u00ca\u00cb\u00cc\u00cd\u00ce\u00cf\u00d0\u00d1\u00d2\u00d3\u00d4\u00d5\u0152\u00d7\u00d8\u00d9\u00da\u00db\u00dc\u00dd\u00de\u00df\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u0153\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u00ed\u00ee\u00ef\u00f0\u00f1\u00f2\u00f3\u00f4\u00f5\u00f6\u00f7\u00f8\u00f9\u00fa\u00fb\u00fc\u00fd\u00fe\u20ac\u0154\u0102\u0139\u0106\u010c\u0118\u011a\u010e\u0110\u0143\u0147\u0150\u0158\u016e\u0170\u0162\u0155\u0103\u013a\u0107\u010d\u0119\u011b\u010f\u0111\u0144\u0148\u0151\u0159\u016f\u0171\u0163\u02d9\u0141\u0104\u015e\u017b\u0142\u0105\u015f\u013d\u013e\u017c\u0160\u015a\u0164\u017d\u0179\u0161\u015b\u0165\u017e\u017a\u00a1\u00a2\u00a4\u00a5\u00a6\u00a7\u00a8\u00a9\u00aa\u00ac\u00ae\u00af\u00b0\u00b1\u00b2\u00b3\u00b4\u00b5\u00b6\u00b7\u00b8\u00b9\u00ba\u00bc\u00bd\u00be\u0410\u0411\u0412\u0413\u0414\u0415\u0416\u0417\u0418\u0419\u041a\u041b\u041c\u041d\u041e\u041f\u0420\u0421\u0422\u0423\u0424\u0425\u0426\u0427\u0428\u0429\u042a\u042b\u042c\u042d\u042e\u042f\u0430\u0431\u0432\u0433\u0434\u0435\u0436\u0437\u0438\u0439\u043a\u043b\u043c\u043d\u043e\u043f\u0440\u0441\u0442\u0443\u0444\u0445\u0446\u0447\u0448\u0449\u044a\u044b\u044c\u044d\u044e\u044f\u0402\u0403\u0453\u0409\u040a\u040c\u040b\u040f\u0452\u0459\u045a\u045c\u045b\u045f\u040e\u045e\u0408\u0490\u0401\u0404\u0407\u0406\u0456\u0491\u0451\u0454\u0458\u0405\u0455\u0457\u2116\u0482\u0391\u0392\u0393\u0394\u0395\u0396\u0397\u0398\u0399\u039a\u039b\u039c\u039d\u039e\u039f\u0390\u03a0\u03a1\u03a3\u03a4\u03a5\u03a6\u03a7\u03a8\u03a9\u03aa\u03ab\u03ac\u03ad\u03ae\u03af\u03b0\u03b1\u03b2\u03b3\u03b4\u03b5\u03b6\u03b7\u03b8\u03b9\u03ba\u03bb\u03bc\u03bd\u03be\u03bf\u03c0\u03c1\u03c2\u03c3\u03c4\u03c5\u03c6\u03c7\u03c8\u03c9\u03ca\u03cb\u03cc\u03cd\u03ce\u0386\u0388\u0389\u038a\u038c\u038e\u038f\u0385";

    public void ProcessJAR(String string, String string_0_, String string_1_,
                           String string_2_)
    {
        try
        {
            byte[] is = new byte[BUFFER_SIZE];
            String string_3_
            = string_1_ + File.separatorChar + new File(string).getName();
            if (new File(string_3_).exists())
                new File(string_3_).delete();
            new File(new File(string_3_).getParent()).mkdirs();
            JarFile jarfile = new JarFile(string_0_);
            JarFile jarfile_4_ = new JarFile(string);
            FileOutputStream fileoutputstream
            = new FileOutputStream(string_3_);
            JarOutputStream jaroutputstream
            = new JarOutputStream(fileoutputstream);
            int i = 0;
            Enumeration enumeration = jarfile_4_.entries();
            while (enumeration.hasMoreElements())
            {
                JarEntry jarentry = (JarEntry) enumeration.nextElement();
                if (!jarentry.isDirectory())
                {
                    if (jarentry.getName().endsWith(".class"))
                    {
                        i++;
                        JarEntry jarentry_5_
                        = jarfile.getJarEntry(jarentry.getName().replace
                                              ('/', File.separatorChar));
                        if (jarentry_5_ != null
                                || jarentry.getName()
                                .endsWith("SymbolPopup.class"))
                        {
                            ByteArrayOutputStream bytearrayoutputstream;
                            if (jarentry_5_ == null)
                                bytearrayoutputstream
                                = (ProcessClass
                                   (jarfile_4_.getInputStream(jarentry),
                                    jarentry.getName(), null, string_2_,
                                    "", ""));
                            else
                                bytearrayoutputstream
                                = (ProcessClass
                                   (jarfile_4_.getInputStream(jarentry),
                                    jarentry.getName(),
                                    jarfile.getInputStream(jarentry_5_),
                                    string_2_, "", ""));
                            JarEntry jarentry_6_
                            = new JarEntry(jarentry.getName());
                            jarentry_6_.setTime(jarentry.getTime());
                            jaroutputstream.putNextEntry(jarentry_6_);
                            ByteArrayInputStream bytearrayinputstream
                            = (new ByteArrayInputStream
                               (bytearrayoutputstream.toByteArray()));
                            for (;;)
                            {
                                int i_7_
                                = bytearrayinputstream.read(is, 0,
                                                            is.length);
                                if (i_7_ <= 0)
                                    break;
                                jaroutputstream.write(is, 0, i_7_);
                            }
                            bytearrayinputstream.close();
                        }
                        else
                        {
                            System.out
                            .println("Copy as is: " + jarentry.getName());
                            JarEntry jarentry_8_
                            = new JarEntry(jarentry.getName());
                            jarentry_8_.setTime(jarentry.getTime());
                            jaroutputstream.putNextEntry(jarentry_8_);
                            InputStream inputstream
                            = jarfile_4_.getInputStream(jarentry);
                            for (;;)
                            {
                                int i_9_ = inputstream.read(is, 0, is.length);
                                if (i_9_ <= 0)
                                    break;
                                jaroutputstream.write(is, 0, i_9_);
                            }
                            inputstream.close();
                        }
                    }
                    else
                    {
                        JarEntry jarentry_10_
                        = jarfile.getJarEntry(jarentry.getName().replace
                                              ('/', File.separatorChar));
                        if (jarentry_10_ != null)
                        {
                            System.out.println("Copying already translated: "
                                               + jarentry.getName());
                            JarEntry jarentry_11_
                            = new JarEntry(jarentry.getName());
                            jarentry_11_.setTime(jarentry.getTime());
                            jaroutputstream.putNextEntry(jarentry_11_);
                            InputStream inputstream
                            = jarfile.getInputStream(jarentry_10_);
                            for (;;)
                            {
                                int i_12_ = inputstream.read(is, 0, is.length);
                                if (i_12_ <= 0)
                                    break;
                                jaroutputstream.write(is, 0, i_12_);
                            }
                            inputstream.close();
                        }
                        else
                        {
                            i++;
                            System.out
                            .println("Copying: " + jarentry.getName());
                            JarEntry jarentry_13_
                            = new JarEntry(jarentry.getName());
                            jarentry_13_.setTime(jarentry.getTime());
                            jaroutputstream.putNextEntry(jarentry_13_);
                            InputStream inputstream
                            = jarfile_4_.getInputStream(jarentry);
                            for (;;)
                            {
                                int i_14_ = inputstream.read(is, 0, is.length);
                                if (i_14_ <= 0)
                                    break;
                                jaroutputstream.write(is, 0, i_14_);
                            }
                            inputstream.close();
                        }
                    }
                }
                else
                    jaroutputstream.putNextEntry(jarentry);
            }
            jaroutputstream.close();
            fileoutputstream.close();
            jarfile_4_.close();
            jarfile.close();
            System.out.println("Processed: " + new Integer(i).toString());
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public void ProcessJAR4(String Jar, String TranslationJar,
                            String ParentDir, String FromLocale,
                            String ToLocale)
    {
        try
        {
            byte[] is = new byte[BUFFER_SIZE];
            String toLocale = ToLocale;
            if (toLocale.equals("de"))
            {
                toLocale = "de_DE";
            }
            String TargetJar = (ParentDir + File.separatorChar
                                + replaceLocale(new File(Jar).getName(),
                                                FromLocale, toLocale));
            if (new File(TargetJar).exists())
                new File(TargetJar).delete();
            new File(new File(TargetJar).getParent()).mkdirs();
            JarFile TranslationJarFile = new JarFile(TranslationJar);
            JarFile InputJarFile = new JarFile(Jar);
            FileOutputStream fileoutputstream
            = new FileOutputStream(TargetJar);
            JarOutputStream jaroutputstream
            = new JarOutputStream(fileoutputstream);
            int i = 0;
            Enumeration enumeration = InputJarFile.entries();
            while (enumeration.hasMoreElements())
            {
                JarEntry InputJarEntry = (JarEntry) enumeration.nextElement();
                if (!InputJarEntry.isDirectory())
                {
                    if (InputJarEntry.getName().endsWith(".class"))
                    {
                        i++;
                        JarEntry TranslationJarEntry
                        = TranslationJarFile.getJarEntry(InputJarEntry.getName());
                        if (TranslationJarEntry != null)
                        {
                            ByteArrayOutputStream bytearrayoutputstream
                            = (ProcessClass
                               (InputJarFile.getInputStream(InputJarEntry),
                                InputJarEntry.getName(),
                                TranslationJarFile.getInputStream(TranslationJarEntry), "",
                                FromLocale, ToLocale));
                            JarEntry TargetJarEntry
                            = new JarEntry(replaceLocale(InputJarEntry.getName(),
                                                         FromLocale,
                                                         ToLocale));
                            TargetJarEntry.setTime(InputJarEntry.getTime());
                            jaroutputstream.putNextEntry(TargetJarEntry);
                            ByteArrayInputStream bytearrayinputstream
                            = (new ByteArrayInputStream
                               (bytearrayoutputstream.toByteArray()));
                            for (;;)
                            {
                                int i_23_
                                = bytearrayinputstream.read(is, 0,
                                                            is.length);
                                if (i_23_ <= 0)
                                    break;
                                jaroutputstream.write(is, 0, i_23_);
                            }
                            bytearrayinputstream.close();
                        }
                        else
                        {
                            String classReady = InputJarEntry.getName().substring(0, InputJarEntry.getName().indexOf(".class"));
                            if (classReady.indexOf('_') == -1)
                            {
                                TranslationJarEntry = null;
                            }
                            else
                            {
                                classReady = classReady.substring(0, classReady.indexOf('_')) + "_" + ToLocale + ".class";
                                TranslationJarEntry
                                = TranslationJarFile.getJarEntry(classReady);
                            }
                            if (TranslationJarEntry != null)
                            {
                                System.out
                                .println("Ready class found, copying as is: " +  classReady);
                                JarEntry jarentryready
                                = new JarEntry(classReady);
                                jarentryready.setTime(InputJarEntry.getTime());
                                jaroutputstream.putNextEntry(jarentryready);
                                InputStream inputstream
                                = TranslationJarFile.getInputStream(TranslationJarEntry);
                                for (;;)
                                {
                                    int i_28_ = inputstream.read(is, 0, is.length);
                                    if (i_28_ <= 0)
                                        break;
                                    jaroutputstream.write(is, 0, i_28_);
                                }
                                inputstream.close();
                            }
                            else
                            {
                                ByteArrayOutputStream bytearrayoutputstream
                                = (ReplaceLangName
                                   (InputJarFile.getInputStream(InputJarEntry),
                                    InputJarEntry.getName(), FromLocale,
                                    ToLocale));
                                JarEntry jarentry_24_
                                = new JarEntry(replaceLocale(InputJarEntry.getName(),
                                                             FromLocale,
                                                             ToLocale));
                                jarentry_24_.setTime(InputJarEntry.getTime());
                                jaroutputstream.putNextEntry(jarentry_24_);
                                ByteArrayInputStream bytearrayinputstream
                                = (new ByteArrayInputStream
                                   (bytearrayoutputstream.toByteArray()));
                                for (;;)
                                {
                                    int i_25_
                                    = bytearrayinputstream.read(is, 0,
                                                                is.length);
                                    if (i_25_ <= 0)
                                        break;
                                    jaroutputstream.write(is, 0, i_25_);
                                }
                                bytearrayinputstream.close();
                            }
                        }
                    }
                    else
                    {
                        JarEntry jarentry_26_
                        = TranslationJarFile.getJarEntry(InputJarEntry.getName());
                        if (jarentry_26_ != null)
                        {
                            System.out.println("Copying already translated: "
                                               + replaceLocale(InputJarEntry.getName(),
                                                               FromLocale,
                                                               ToLocale));
                            JarEntry jarentry_27_
                            = new JarEntry(replaceLocale(InputJarEntry.getName(),
                                                         FromLocale,
                                                         ToLocale));
                            jarentry_27_.setTime(InputJarEntry.getTime());
                            jaroutputstream.putNextEntry(jarentry_27_);
                            InputStream inputstream
                            = TranslationJarFile.getInputStream(jarentry_26_);
                            for (;;)
                            {
                                int i_28_ = inputstream.read(is, 0, is.length);
                                if (i_28_ <= 0)
                                    break;
                                jaroutputstream.write(is, 0, i_28_);
                            }
                            inputstream.close();
                        }
                        else if ((InputJarEntry.getName().indexOf("INDEX.LIST")
                                  != -1)
                                 || InputJarEntry.getName()
                                 .indexOf("MANIFEST.MF") != -1)
                        {
                            i++;
                            System.out
                            .println("Copying: " + InputJarEntry.getName());
                            JarEntry jarentry_29_
                            = new JarEntry(replaceLocale(InputJarEntry.getName(),
                                                         FromLocale,
                                                         ToLocale));
                            jarentry_29_.setTime(InputJarEntry.getTime());
                            jaroutputstream.putNextEntry(jarentry_29_);
                            ProcessRegularTextFile
                            (InputJarFile.getInputStream(InputJarEntry),
                             jaroutputstream, FromLocale, ToLocale);
                        }
                        else
                        {
                            i++;
                            System.out
                            .println("Copying: " + InputJarEntry.getName());
                            JarEntry jarentry_30_
                            = new JarEntry(replaceLocale(InputJarEntry.getName(),
                                                         FromLocale,
                                                         ToLocale));
                            jarentry_30_.setTime(InputJarEntry.getTime());
                            jaroutputstream.putNextEntry(jarentry_30_);
                            InputStream inputstream
                            = InputJarFile.getInputStream(InputJarEntry);
                            for (;;)
                            {
                                int i_31_ = inputstream.read(is, 0, is.length);
                                if (i_31_ <= 0)
                                    break;
                                jaroutputstream.write(is, 0, i_31_);
                            }
                            inputstream.close();
                        }
                    }
                }
                else
                    jaroutputstream.putNextEntry(InputJarEntry);
            }
            jaroutputstream.close();
            fileoutputstream.close();
            InputJarFile.close();
            TranslationJarFile.close();
            System.out.println("Processed: " + new Integer(i).toString());
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public static String replace(String string, String string_32_,
                                 String string_33_)
    {
        if (string != null)
        {
            int i = string_32_.length();
            StringBuffer stringbuffer = new StringBuffer();
            int i_34_ = -1;
            int i_35_;
            for (i_35_ = 0; (i_34_ = string.indexOf(string_32_, i_35_)) != -1;
                    i_35_ = i_34_ + i)
            {
                stringbuffer.append(string.substring(i_35_, i_34_));
                stringbuffer.append(string_33_);
            }
            stringbuffer.append(string.substring(i_35_));
            return stringbuffer.toString();
        }
        return "";
    }

    public static String replaceLocale(String string, String string_32_,
                                       String string_33_)
    {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ( (e = string.indexOf( string_32_, s ) ) >= 0 )
        {
            result.append(string.substring( s, e ) );
            if (e != 0 && ( string.charAt(e-1) == '-' || string.charAt(e-1) == '_'))
            {
                result.append( string_33_ );
            }
            else
            {
                result.append( string_32_ );

            }
            s = e+string_32_.length();
            if (s < string.length() && string.charAt(s) == '_')
            {
                s+=3;
            }
        }
        result.append( string.substring( s ) );
        return result.toString();
    }

    public JavaClass patchSymbolPopup(JavaClass javaclass)
    {
        ClassGen classgen = new ClassGen(javaclass);
        Method[] methods = javaclass.getMethods();
        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];
            org.apache.bcel.classfile.Code code = methods[i].getCode();
            if (code != null
                    && (methods[i].toString().equals
                        ("public void <init>(com.amazon.ebook.framework.gui.foundation.b a, boolean a, boolean a)")))
            {
                System.out.println("[SymbolPopup] Enabling ALT symbols :)");
                ConstantPoolGen constantpoolgen = classgen.getConstantPool();
                MethodGen methodgen
                = new MethodGen(method, classgen.getClassName(),
                                constantpoolgen);
                InstructionList instructionlist
                = methodgen.getInstructionList();
                InstructionFactory instructionfactory
                = new InstructionFactory(classgen);
                instructionlist.insert(instructionlist.findHandle(4),
                                       new ALOAD(0));
                instructionlist.append
                (instructionlist.insert(instructionlist.findHandle(5),
                                        new ICONST(1)),
                 (instructionfactory.createPutStatic
                  ("com.amazon.ebook.framework.gui.foundation.SymbolPopup",
                   "ADVANCED_MODE_CONFIGURED", Type.BOOLEAN)));
                instructionlist.setPositions();
                methodgen.setInstructionList(instructionlist);
                methodgen.setMaxStack();
                methodgen.setMaxLocals();
                methodgen.removeLineNumbers();
                classgen.replaceMethod(method, methodgen.getMethod());
            }
        }
        return classgen.getJavaClass();
    }

    public ByteArrayOutputStream ProcessClass
    (InputStream inputstream, String string, InputStream inputstream_36_,
     String string_37_, String string_38_, String string_39_)
    throws IOException
    {
        Object object = null;
        JavaClass javaclass = null;
        Object object_40_ = null;
        HashMap hashmap = new HashMap();
        ByteArrayOutputStream bytearrayoutputstream
        = new ByteArrayOutputStream();
        boolean bool = false;
        if (inputstream_36_ != null)
        {
            System.out.print("Translating: " + string + "...");
            BufferedReader bufferedreader
            = new BufferedReader(new InputStreamReader(inputstream_36_,
                                 "UTF-8"));
            String string_41_;
            while ((string_41_ = bufferedreader.readLine()) != null)
            {
                if (string_41_.indexOf("\t") > 0)
                {
                    String string_42_
                    = string_41_.substring(0, string_41_.indexOf("\t"));
                    String string_43_
                    = string_41_.substring(string_41_.indexOf("\t") + 1,
                                           string_41_.length());
                    hashmap.put(string_42_, string_43_);
                }
            }
            ClassParser classparser = new ClassParser(inputstream, string);
            try
            {
                javaclass = classparser.parse();
            }
            catch (ClassFormatException classformatexception)
            {
                classformatexception.printStackTrace();
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
            }
            ConstantPool constantpool = javaclass.getConstantPool();
            for (int i = 0; i < constantpool.getLength(); i++)
            {
                Constant constant = constantpool.getConstant(i);
                if (constant != null && constant.getTag() == 1)
                {
                    ConstantUtf8 constantutf8 = (ConstantUtf8) constant;
                    String string_44_
                    = replace(replace(constantutf8.getBytes(), "\n", "~~"),
                              "\r", "$$");
                    if (string_37_.length() != 0
                            && string_44_.equals(cAltSymbols))
                    {
                        constantutf8.setBytes(string_37_);
                        constantpool.setConstant(i, constantutf8);
                        bool = true;
                    }
                    else if (hashmap.containsKey(string_44_))
                    {
                        constantutf8.setBytes
                        (replace(replace((String) hashmap.get(string_44_),
                                         "~~", "\n"),
                                 "$$", "\r"));
                        constantpool.setConstant(i, constantutf8);
                        bool = true;
                    }
                    else if (string_38_.length() > 0
                             && (constantutf8.getBytes().indexOf(string_38_)
                                 != -1))
                    {
                        String string_45_ = replace(constantutf8.getBytes(),
                                                    string_38_, string_39_);
                        constantutf8.setBytes(string_45_);
                        constantpool.setConstant(i, constantutf8);
                        bool = true;
                    }
                }
            }
            javaclass.dump(bytearrayoutputstream);
            if (bool)
                System.out.println(" translated succesfully.");
            else
                System.out.println(" Not modified.");
        }
        else
        {
            System.out.println("Patching: " + string + "...");
            ClassParser classparser = new ClassParser(inputstream, string);
            try
            {
                javaclass = classparser.parse();
            }
            catch (ClassFormatException classformatexception)
            {
                classformatexception.printStackTrace();
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
            }
            JavaClass javaclass_46_ = patchSymbolPopup(javaclass);
            if (javaclass_46_ != null)
                javaclass_46_.dump(bytearrayoutputstream);
            else
                javaclass.dump(bytearrayoutputstream);
        }
        return bytearrayoutputstream;
    }

    public ByteArrayOutputStream ReplaceLangName
    (InputStream inputstream, String string, String string_47_,
     String string_48_)
    throws IOException
    {
        Object object = null;
        JavaClass javaclass = null;
        Object object_49_ = null;
        ByteArrayOutputStream bytearrayoutputstream
        = new ByteArrayOutputStream();
        boolean bool = false;
        System.out.print("Copy and replace lang name: " + string + "...");
        ClassParser classparser = new ClassParser(inputstream, string);
        try
        {
            javaclass = classparser.parse();
        }
        catch (ClassFormatException classformatexception)
        {
            classformatexception.printStackTrace();
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        ConstantPool constantpool = javaclass.getConstantPool();
        for (int i = 0; i < constantpool.getLength(); i++)
        {
            Constant constant = constantpool.getConstant(i);
            if (constant != null && constant.getTag() == 1)
            {
                ConstantUtf8 constantutf8 = (ConstantUtf8) constant;
                if (constantutf8.toString().indexOf(string_47_) != -1)
                {
                    String string_50_ = replace(constantutf8.toString(),
                                                string_47_, string_48_);
                    constantutf8.setBytes(string_50_);
                    constantpool.setConstant(i, constantutf8);
                    bool = true;
                }
            }
        }
        javaclass.dump(bytearrayoutputstream);
        if (bool)
            System.out.println(" translated succesfully.");
        else
            System.out.println(" Not modified.");
        return bytearrayoutputstream;
    }

    public void ProcessKEYB(String string, String string_51_,
                            String string_52_)
    {
        try
        {
            byte[] is = new byte[BUFFER_SIZE];
            String string_53_
            = string_51_ + File.separatorChar + new File(string).getName();
            if (new File(string_53_).exists())
                new File(string_53_).delete();
            new File(new File(string_53_).getParent()).mkdirs();
            JarFile jarfile = new JarFile(string);
            FileOutputStream fileoutputstream
            = new FileOutputStream(string_53_);
            JarOutputStream jaroutputstream
            = new JarOutputStream(fileoutputstream);
            int i = 0;
            Enumeration enumeration = jarfile.entries();
            while (enumeration.hasMoreElements())
            {
                JarEntry jarentry = (JarEntry) enumeration.nextElement();
                if (!jarentry.isDirectory())
                {
                    if (jarentry.getName().endsWith("GlobalMenuItems.class")
                            || jarentry.getName()
                            .endsWith("StatusBarResources_en.class")
                            || jarentry.getName().endsWith("SymbolPopup.class"))
                    {
                        ByteArrayOutputStream bytearrayoutputstream;
                        if (jarentry.getName().endsWith("SymbolPopup.class"))
                            bytearrayoutputstream
                            = ProcessClass(jarfile
                                           .getInputStream(jarentry),
                                           jarentry.getName(), null, "",
                                           "", "");
                        else if (jarentry.getName()
                                 .endsWith("GlobalMenuItems.class"))
                            bytearrayoutputstream
                            = (ProcessSingle
                               (jarfile.getInputStream(jarentry),
                                jarentry.getName(),
                                "\u00bf\u00c0\u00c1\u00c2\u00c3\u00c4\u00c5\u00c6\u00c7\u00c8\u00c9\u00ca\u00cb\u00cc\u00cd\u00ce\u00cf\u00d0\u00d1\u00d2\u00d3\u00d4\u00d5\u0152\u00d7\u00d8\u00d9\u00da\u00db\u00dc\u00dd\u00de\u00df\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u0153\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u00ed\u00ee\u00ef\u00f0\u00f1\u00f2\u00f3\u00f4\u00f5\u00f6\u00f7\u00f8\u00f9\u00fa\u00fb\u00fc\u00fd\u00fe\u20ac\u0154\u0102\u0139\u0106\u010c\u0118\u011a\u010e\u0110\u0143\u0147\u0150\u0158\u016e\u0170\u0162\u0155\u0103\u013a\u0107\u010d\u0119\u011b\u010f\u0111\u0144\u0148\u0151\u0159\u016f\u0171\u0163\u02d9\u0141\u0104\u015e\u017b\u0142\u0105\u015f\u013d\u013e\u017c\u0160\u015a\u0164\u017d\u0179\u0161\u015b\u0165\u017e\u017a\u00a1\u00a2\u00a4\u00a5\u00a6\u00a7\u00a8\u00a9\u00aa\u00ac\u00ae\u00af\u00b0\u00b1\u00b2\u00b3\u00b4\u00b5\u00b6\u00b7\u00b8\u00b9\u00ba\u00bc\u00bd\u00be\u0410\u0411\u0412\u0413\u0414\u0415\u0416\u0417\u0418\u0419\u041a\u041b\u041c\u041d\u041e\u041f\u0420\u0421\u0422\u0423\u0424\u0425\u0426\u0427\u0428\u0429\u042a\u042b\u042c\u042d\u042e\u042f\u0430\u0431\u0432\u0433\u0434\u0435\u0436\u0437\u0438\u0439\u043a\u043b\u043c\u043d\u043e\u043f\u0440\u0441\u0442\u0443\u0444\u0445\u0446\u0447\u0448\u0449\u044a\u044b\u044c\u044d\u044e\u044f\u0402\u0403\u0453\u0409\u040a\u040c\u040b\u040f\u0452\u0459\u045a\u045c\u045b\u045f\u040e\u045e\u0408\u0490\u0401\u0404\u0407\u0406\u0456\u0491\u0451\u0454\u0458\u0405\u0455\u0457\u2116\u0482\u0391\u0392\u0393\u0394\u0395\u0396\u0397\u0398\u0399\u039a\u039b\u039c\u039d\u039e\u039f\u0390\u03a0\u03a1\u03a3\u03a4\u03a5\u03a6\u03a7\u03a8\u03a9\u03aa\u03ab\u03ac\u03ad\u03ae\u03af\u03b0\u03b1\u03b2\u03b3\u03b4\u03b5\u03b6\u03b7\u03b8\u03b9\u03ba\u03bb\u03bc\u03bd\u03be\u03bf\u03c0\u03c1\u03c2\u03c3\u03c4\u03c5\u03c6\u03c7\u03c8\u03c9\u03ca\u03cb\u03cc\u03cd\u03ce\u0386\u0388\u0389\u038a\u038c\u038e\u038f\u0385",
                                string_52_));
                        else
                            bytearrayoutputstream
                            = ProcessSingle(jarfile
                                            .getInputStream(jarentry),
                                            jarentry.getName(), "h:mm a",
                                            "HH:mm");
                        JarEntry jarentry_54_
                        = new JarEntry(jarentry.getName());
                        jarentry_54_.setTime(jarentry.getTime());
                        jaroutputstream.putNextEntry(jarentry_54_);
                        ByteArrayInputStream bytearrayinputstream
                        = new ByteArrayInputStream(bytearrayoutputstream
                                                   .toByteArray());
                        for (;;)
                        {
                            int i_55_
                            = bytearrayinputstream.read(is, 0, is.length);
                            if (i_55_ <= 0)
                                break;
                            jaroutputstream.write(is, 0, i_55_);
                        }
                        bytearrayinputstream.close();
                    }
                    else
                    {
                        i++;
                        JarEntry jarentry_56_
                        = new JarEntry(jarentry.getName());
                        jarentry_56_.setTime(jarentry.getTime());
                        jaroutputstream.putNextEntry(jarentry_56_);
                        InputStream inputstream
                        = jarfile.getInputStream(jarentry);
                        for (;;)
                        {
                            int i_57_ = inputstream.read(is, 0, is.length);
                            if (i_57_ <= 0)
                                break;
                            jaroutputstream.write(is, 0, i_57_);
                        }
                        inputstream.close();
                    }
                }
                else
                    jaroutputstream.putNextEntry(jarentry);
            }
            jaroutputstream.close();
            fileoutputstream.close();
            jarfile.close();
            System.out.println("Processed: " + new Integer(i).toString());
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public ByteArrayOutputStream ProcessSingle
    (InputStream inputstream, String string, String string_58_,
     String string_59_)
    throws IOException
    {
        Object object = null;
        JavaClass javaclass = null;
        Object object_60_ = null;
        ByteArrayOutputStream bytearrayoutputstream
        = new ByteArrayOutputStream();
        boolean bool = false;
        System.out.print("Translating single line in: " + string + "...");
        ClassParser classparser = new ClassParser(inputstream, string);
        try
        {
            javaclass = classparser.parse();
        }
        catch (ClassFormatException classformatexception)
        {
            classformatexception.printStackTrace();
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        ConstantPool constantpool = javaclass.getConstantPool();
        for (int i = 0; i < constantpool.getLength(); i++)
        {
            Constant constant = constantpool.getConstant(i);
            if (constant != null && constant.getTag() == 1)
            {
                ConstantUtf8 constantutf8 = (ConstantUtf8) constant;
                if (constantutf8.getBytes().equals(string_58_))
                {
                    constantutf8.setBytes(string_59_);
                    constantpool.setConstant(i, constantutf8);
                    bool = true;
                }
            }
        }
        javaclass.dump(bytearrayoutputstream);
        if (!bool)
            System.out.println(" String " + string_58_ + " not found.");
        else
            System.out.println(" OK.");
        return bytearrayoutputstream;
    }

    public void ProcessPREFS(String string, String string_61_,
                             String string_62_)
    {
        HashMap hashmap = new HashMap();
        if (new File(string_61_).exists())
            new File(string_61_).delete();
        new File(new File(string_61_).getParent()).mkdirs();
        try
        {
            String string_63_ = "UTF-8";
            FileInputStream fileinputstream = new FileInputStream(string_62_);
            UnicodeInputStream unicodeinputstream
            = new UnicodeInputStream(fileinputstream, string_63_);
            string_63_ = unicodeinputstream.getEncoding();
            InputStreamReader inputstreamreader;
            if (string_63_ == null)
                inputstreamreader = new InputStreamReader(unicodeinputstream);
            else
                inputstreamreader
                = new InputStreamReader(unicodeinputstream, string_63_);
            BufferedReader bufferedreader
            = new BufferedReader(inputstreamreader);
            String string_64_;
            while ((string_64_ = bufferedreader.readLine()) != null)
            {
                if (string_64_.indexOf("=") > 0)
                {
                    String string_65_
                    = string_64_.substring(0, string_64_.indexOf("="));
                    String string_66_
                    = (TranslationPacker.ConvertText
                       (string_64_.substring(string_64_.indexOf("=") + 1,
                                             string_64_.length())));
                    hashmap.put(string_65_, string_66_);
                }
            }
            bufferedreader.close();
            fileinputstream.close();
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        String string_67_ = "";
        try
        {
            BufferedReader bufferedreader
            = (new BufferedReader
               (new InputStreamReader(new FileInputStream(string))));
            String string_68_;
            while ((string_68_ = bufferedreader.readLine()) != null)
            {
                if (string_68_.indexOf("=") > 0)
                {
                    String string_69_
                    = string_68_.substring(0, string_68_.indexOf("="));
                    if (hashmap.containsKey(string_69_))
                        string_68_
                        = string_69_ + "=" + hashmap.get(string_69_);
                }
                string_67_ += (String) string_68_ + '\n';
            }
            bufferedreader.close();
            OutputStreamWriter outputstreamwriter
            = new OutputStreamWriter(new FileOutputStream(string_61_));
            outputstreamwriter.write(string_67_);
            outputstreamwriter.close();
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        System.out.println("File processed!");
    }

    public void ProcessRegularTextFile(InputStream inputstream,
                                       OutputStream outputstream,
                                       String string, String string_70_)
    {
        try
        {
            String string_71_ = "UTF-8";
            UnicodeInputStream unicodeinputstream
            = new UnicodeInputStream(inputstream, string_71_);
            string_71_ = unicodeinputstream.getEncoding();
            InputStreamReader inputstreamreader;
            if (string_71_ == null)
                inputstreamreader = new InputStreamReader(unicodeinputstream);
            else
                inputstreamreader
                = new InputStreamReader(unicodeinputstream, string_71_);
            BufferedReader bufferedreader
            = new BufferedReader(inputstreamreader);
            OutputStreamWriter outputstreamwriter
            = new OutputStreamWriter(outputstream);
            String string_72_;
            while ((string_72_ = bufferedreader.readLine()) != null)
            {
                string_72_ = replace(string_72_, string, string_70_);
                outputstreamwriter.write(string_72_ + '\n');
            }
            bufferedreader.close();
            outputstreamwriter.flush();
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        System.out.println("File processed!");
    }

    public ByteArrayOutputStream ProcessClassKeyb4
    (InputStream inputstream, String string, InputStream inputstream_73_)
    throws IOException
    {
        Object object = null;
        JavaClass javaclass = null;
        Object object_74_ = null;
        HashMap hashmap = new HashMap();
        ByteArrayOutputStream bytearrayoutputstream
        = new ByteArrayOutputStream();
        boolean bool = false;
        if (inputstream_73_ != null)
        {
            System.out.print("Translating: " + string + "...");
            BufferedReader bufferedreader
            = new BufferedReader(new InputStreamReader(inputstream_73_,
                                 "UTF-8"));
            String string_75_;
            while ((string_75_ = bufferedreader.readLine()) != null)
            {
                if (string_75_.indexOf("\t") > 0)
                {
                    String string_76_
                    = string_75_.substring(0, string_75_.indexOf("\t"));
                    String string_77_
                    = string_75_.substring(string_75_.indexOf("\t") + 1,
                                           string_75_.length());
                    hashmap.put(string_76_, string_77_);
                }
            }
            ClassParser classparser = new ClassParser(inputstream, string);
            try
            {
                javaclass = classparser.parse();
            }
            catch (ClassFormatException classformatexception)
            {
                classformatexception.printStackTrace();
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
            }
            ConstantPool constantpool = javaclass.getConstantPool();
            for (int i = 0; i < constantpool.getLength(); i++)
            {
                Constant constant = constantpool.getConstant(i);
                if (constant != null && constant.getTag() == 1)
                {
                    ConstantUtf8 constantutf8 = (ConstantUtf8) constant;
                    String string_78_ = constantutf8.getBytes();
                    String string_79_ = string_78_;
                    Iterator iterator = hashmap.keySet().iterator();
                    boolean bool_80_ = false;
                    while (iterator.hasNext())
                    {
                        String string_81_ = (String) iterator.next();
                        if (string_79_.indexOf(string_81_) != -1)
                        {
                            string_79_
                            = replace(string_79_, string_81_,
                                      (String) hashmap.get(string_81_));
                            bool_80_ = true;
                        }
                    }
                    if (bool_80_)
                    {
                        constantutf8.setBytes(string_79_);
                        constantpool.setConstant(i, constantutf8);
                        bool = true;
                    }
                }
            }
            javaclass.dump(bytearrayoutputstream);
            if (bool)
                System.out.println(" translated succesfully.");
            else
                System.out.println(" Not modified.");
        }
        else
        {
            System.out.println("Patching: " + string + "...");
            ClassParser classparser = new ClassParser(inputstream, string);
            try
            {
                javaclass = classparser.parse();
            }
            catch (ClassFormatException classformatexception)
            {
                classformatexception.printStackTrace();
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
            }
            JavaClass javaclass_82_ = patchSymbolPopup(javaclass);
            if (javaclass_82_ != null)
                javaclass_82_.dump(bytearrayoutputstream);
            else
                javaclass.dump(bytearrayoutputstream);
        }
        return bytearrayoutputstream;
    }

    public void ProcessKEYB4(String string, String string_83_,
                             String string_84_, String string_85_)
    {
        try
        {
            byte[] is = new byte[BUFFER_SIZE];
            String string_86_
            = (new File(string_83_).getParent() + File.separatorChar
               + replace(new File(string_83_).getName(), ".jar",
                         "-" + string_85_ + ".jar"));
            System.out.println("Patching: " + string_86_ + "...");
            JarFile jarfile = new JarFile(string_83_);
            int i = 0;
            JarEntry jarentry
            = (jarfile.getJarEntry
               ("com/amazon/ebook/framework/resources/KeyboardBaseResources.class"));
            if (jarentry != null)
            {
                FileInputStream fileinputstream = new FileInputStream(string);
                ByteArrayOutputStream bytearrayoutputstream
                = ProcessClassKeyb4(jarfile.getInputStream(jarentry),
                                    jarentry.getName(), fileinputstream);
                JarInputStream jarinputstream
                = new JarInputStream(new FileInputStream(string_86_));
                JarOutputStream jaroutputstream
                = (new JarOutputStream
                   (new FileOutputStream(replace(string_86_, ".jar",
                                                 ".tmp"))));
                JarEntry jarentry_87_ = jarinputstream.getNextJarEntry();
                byte[] is_88_ = new byte[1024];
                for (/**/; jarentry_87_ != null;
                         jarentry_87_ = jarinputstream.getNextJarEntry())
                {
                    jaroutputstream.putNextEntry(jarentry_87_);
                    int i_89_;
                    while ((i_89_ = jarinputstream.read(is_88_)) > 0)
                        jaroutputstream.write(is_88_, 0, i_89_);
                }
                JarEntry jarentry_90_
                = new JarEntry(replace(jarentry.getName(), ".class",
                                       "_" + string_85_ + ".class"));
                jarentry_90_.setTime(jarentry.getTime());
                jaroutputstream.putNextEntry(jarentry_90_);
                ByteArrayInputStream bytearrayinputstream
                = new ByteArrayInputStream(bytearrayoutputstream
                                           .toByteArray());
                for (;;)
                {
                    int i_91_ = bytearrayinputstream.read(is, 0, is.length);
                    if (i_91_ <= 0)
                        break;
                    jaroutputstream.write(is, 0, i_91_);
                }
                bytearrayinputstream.close();
                jaroutputstream.close();
                jarinputstream.close();
                new File(string_86_).delete();
                new File(replace(string_86_, ".jar", ".tmp"))
                .renameTo(new File(string_86_));
            }
            jarfile.close();
            System.out.println("Processed: " + new Integer(i).toString());
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }
}
