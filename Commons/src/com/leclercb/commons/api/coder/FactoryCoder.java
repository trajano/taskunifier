/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.commons.api.coder;

import java.io.InputStream;
import java.io.OutputStream;

import com.leclercb.commons.api.coder.exc.FactoryCoderException;

/**
 * Defines the decode and encode protocol of a factory.
 * 
 * @author Benjamin Leclerc
 */
public interface FactoryCoder {
	
	/**
	 * Decodes a factory from the input stream. All the decoded objects will be
	 * created (and inserted) by the factory.
	 * 
	 * @param input
	 *            input stream
	 * @throws FactoryCoderException
	 *             if an error occurs during decoding
	 */
	public abstract void decode(InputStream input) throws FactoryCoderException;
	
	/**
	 * Encodes a factory in the output stream. All the encoded objects come from
	 * the factory.
	 * 
	 * @param output
	 *            output stream
	 * @throws FactoryCoderException
	 *             if an error occurs during encoding
	 */
	public abstract void encode(OutputStream output)
			throws FactoryCoderException;
	
}
